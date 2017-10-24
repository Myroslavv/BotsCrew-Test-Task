package library.database;

import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 * This class implements common to all hibernate connectors method.
 * <p> As a result - derivative classes are not forced 
 * to repeatedly implement the same code
 * </p>
 * @author Myroslav
 *
 */
public abstract class HibernateAbstractConnector implements HibernateConnector
{
	protected String configurationFileName = "hibernate.cfg.xml";
	
	public HibernateAbstractConnector()
	{
	}
	
	public SessionFactory getConnection()
	{
		try
		{
			SessionFactory factory = new Configuration()
					.configure(configurationFileName)
					.buildSessionFactory();
			return factory;
		}
		catch(Throwable ex)
		{
			throw new HibernateException(ex);
		}
	}
}
