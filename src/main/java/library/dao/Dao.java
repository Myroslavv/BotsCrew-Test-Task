package library.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Defines basic CRUD operations
 * @author Myroslav
 *
 * @param <T>	Persistent type
 * @param <PK>	Primary key of persistent object
 */
public interface Dao<T, PK extends Serializable>
{
	boolean create(T obj);
	T read(PK key);
	boolean update(T obj);
	boolean delete(T obj);
	
	List<T> readAll();
}
