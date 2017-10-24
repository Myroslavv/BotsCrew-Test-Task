package library.controller;

import library.dao.AuthorDao;
import library.dao.BookDao;

/**
 * Interface, which represents concrete <code>Controller</code> for operations 
 * on <code>Book</code>.
 * @author Myroslav
 *
 */
public interface BookController extends Controller, ControllerWithRepresentation
{
	void showByTitle();
	void setBookDao(BookDao dao);
	void setAuthorDao(AuthorDao dao);
}
