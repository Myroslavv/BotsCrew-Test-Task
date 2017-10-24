package library.dao;

import library.model.Author;

/**
 * Specifies operations for concrete access object to <code>Author</code> data
 * @author Myroslav
 *
 */
public interface AuthorDao extends Dao<Author, Long>
{
	/**
	 * 
	 * @param name	Author name attribute.
	 * @return		Null, if there is no author with passed name. 
	 * 				In other case - author, whose <code>name</code> 
	 * 				attribute equals to passed.
	 */
	Author readByName(String name);
}
