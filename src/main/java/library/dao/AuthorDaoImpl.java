package library.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import library.model.Author;

/**
 * This class implements method, specific for <code>Author</code> persistent class
 * and inherits basic operations (CRUD + read All).
 * @author Myroslav
 *
 */
public class AuthorDaoImpl extends AbstractDao<Author, Long>
				implements AuthorDao
{
	public AuthorDaoImpl(SessionFactory factory) 
	{
		super(Author.class, factory);
	}
	
	@Override
	public Author readByName(String name) 
	{
		List<Author> customSearchResult = getListBySingleRequirement("name", name);
		
		if (customSearchResult.isEmpty())
			return null;
		
		return customSearchResult.get(0);
	}	
}
