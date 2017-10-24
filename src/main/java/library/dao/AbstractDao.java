package library.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * Implements basic CRUD operations 
 * and represent template for more derivative Data Access Objects.
 * <p>The purpose of this class is also avoiding repeating code.
 * </p>
 * @author Myroslav
 *
 * @param <T>	Persistent type
 * @param <PK>	Primary key of persistent object
 */
public abstract class AbstractDao<T, PK extends Serializable> implements Dao<T, PK>
{
	protected Class<T> entityClass;
	protected SessionFactory sessionFactory;
	
	protected AbstractDao(Class<T> entityClass, SessionFactory factory) 
	{
		this.sessionFactory = Objects.requireNonNull(factory);
		this.entityClass = Objects.requireNonNull(entityClass);
	}
	
	@Override
	public boolean create(T model) 
	{
		Objects.requireNonNull(model);
		
		boolean result = false;
		Transaction transaction = null;
		try(Session session = sessionFactory.openSession())
		{
			transaction = session.beginTransaction();
			
			session.persist(model);
			
			transaction.commit();
			result = true;
		}
		catch(Throwable ex)
		{
			ex.printStackTrace();
			
			try
			{
				if (transaction != null)
					transaction.rollback();
			}
			catch(Throwable innerEx)
			{
				innerEx.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public T read(PK key) 
	{
		Transaction transaction = null;
		try(Session session = sessionFactory.openSession())
		{
			transaction = session.beginTransaction();
			
			T result = session.get(entityClass, key);
			
			transaction.commit();
			return result;
		}
		catch(Throwable ex)
		{
			if (transaction != null)
				transaction.rollback();
			
			throw ex;
		}
	}
	
	@Override
	public boolean update(T model) 
	{
		Objects.requireNonNull(model);
		
		Transaction transaction = null;
		try(Session session = sessionFactory.openSession())
		{
			transaction = session.beginTransaction();
			
			session.merge(model);
			
			transaction.commit();
			return true;
		}
		catch(Throwable ex)
		{
			if (transaction != null)
				transaction.rollback();
		}
		return false;
	}
	
	@Override
	public boolean delete(T model) 
	{
		Objects.requireNonNull(model);
		
		Transaction transaction = null;
		try(Session session = sessionFactory.openSession())
		{
			transaction = session.beginTransaction();
			
			session.delete(model);
			
			transaction.commit();
			return true;
		}
		catch(Throwable ex)
		{
			if (transaction != null)
				transaction.rollback();
		}
		return false;
	}
	
	@Override
	public List<T> readAll() 
	{
		List<T> allPersistedEntitites = new ArrayList<>();
		
		Transaction transaction = null;
		
		try(Session session = sessionFactory.openSession())
		{
			transaction = session.beginTransaction();
			
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
			Root<T> entityRoot = criteriaQuery.from(entityClass);
			criteriaQuery.select(entityRoot);
			
			Query<T> query = session.createQuery(criteriaQuery);
			allPersistedEntitites = query.getResultList();
			
			transaction.commit();
		}
		catch(Throwable ex)
		{
			if (transaction != null)
				transaction.rollback();
		}
		
		return allPersistedEntitites;
	}
	
	/**
	 * Created for further usage in derived classes for more specified search
	 * @param requirements 	Map of fields names and their desired values
	 * @param <P> 			Describes type of field, passed as desired value
	 * @return				List of objects whose attributes meet passed requirements
	 */
	protected <P> List<T> readByEqualityCriteria(Map<String, P> requirements)
	{	
		if (requirementsAreNullOrEmpty(requirements))
			Collections.emptyList();
		
		List<T> result = new ArrayList<>();
		Transaction transaction = null;
		
		try(Session session = sessionFactory.openSession())
		{
			transaction = session.beginTransaction();
			
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
			Root<T> entityRoot = criteriaQuery.from(entityClass);
			criteriaQuery.select(entityRoot);
			
			requirements.forEach((fieldName, fieldValue) -> 
			{
				criteriaQuery.where(criteriaBuilder.equal(entityRoot.get(fieldName), fieldValue));
			});
			
			Query<T> query = session.createQuery(criteriaQuery);
			result = query.getResultList();
			
			transaction.commit();
		}
		catch(Throwable ex)
		{
			if (transaction != null)
				transaction.rollback();
		}
		
		return result;
	}
	
	/**
	 * Checks if requirements, passed to custom query, are valid to use
	 * @param requirements 	The list of fields and their desired values
	 * @param <P> 			Describes type of field, passed as desired value
	 * @return				Are passed requirements are null reference or empty map
	 */
	private <P> boolean requirementsAreNullOrEmpty(Map<String, P> requirements)
	{
		try
		{
			return Objects.requireNonNull(requirements).isEmpty();
		}
		catch(Throwable ex)
		{
			return true;
		}
	}
	
	protected <P> List<T> getListBySingleRequirement(String requirementName, P requirementValue)
	{
		Map<String, P> customRequirement = new HashMap<>();
		customRequirement.put(requirementName, requirementValue);
		
		return readByEqualityCriteria(customRequirement);
	}
}