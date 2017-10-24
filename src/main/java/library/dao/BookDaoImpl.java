package library.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import library.model.Book;

/**
 * This class implements method, specific for <code>Book</code> persistent class
 * and inherits basic operations (CRUD + read All).
 * @author Myroslav
 *
 */
public class BookDaoImpl extends AbstractDao<Book, Long>
				implements BookDao
{
	public BookDaoImpl(SessionFactory factory)
	{
		super(Book.class, factory);
	}

	@Override
	public List<Book> readByTitle(String title) 
	{
		List<Book> customSearchResult = getListBySingleRequirement("title", title);
		return customSearchResult;
	}
}
