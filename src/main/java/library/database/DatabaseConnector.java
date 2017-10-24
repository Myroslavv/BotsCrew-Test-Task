package library.database;

/**
 * This interface is the entry point to database. 
 * @author Myroslav
 *
 * @param <T>	"Carrirer" of connection.
 */
public interface DatabaseConnector<T>
{
	/**
	 * 
	 * @return	Suitable for chosen framework type.
	 */
	T getConnection();
}
