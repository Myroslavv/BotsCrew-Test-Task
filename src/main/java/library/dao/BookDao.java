package library.dao;

import java.util.List;

import library.model.Book;

/**
 * Specifies operations for concrete access object to <code>Book</code> data
 * @author Myroslav
 *
 */
public interface BookDao extends Dao<Book, Long>
{
	/**
	 * Searches and returns the books with similar title(name).
	 * @param 	title	Desired title of books.
	 * @return	Empty list if there is no such book.
	 * 			List of Book instances, which <code>title</code> equals to passed.
	 */
	List<Book> readByTitle(String title);
}
