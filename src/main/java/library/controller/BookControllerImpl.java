package library.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import library.dao.AuthorDao;
import library.dao.BookDao;
import library.model.Author;
import library.model.Book;
import library.representation.ConsoleRepresentation;

/**
 * Class consists of <code>DAO</code> and possible way to interact with user.
 * 
 * <p>This class targets <code>Book</code> entity.
 * </p>
 * @author Myroslav
 *
 */
public class BookControllerImpl implements BookController
{
	private BookDao bookDao;
	private AuthorDao authorDao;
	private ConsoleRepresentation consoleRepresentation;
	
	public BookControllerImpl(BookDao bookDao, AuthorDao authorDao, ConsoleRepresentation representation) 
	{
		this.bookDao = bookDao;
		this.authorDao = authorDao;
		this.consoleRepresentation = representation;
	}
	
	public void setBookDao(BookDao dao)
	{
		this.bookDao = dao;
	}
	
	public void setAuthorDao(AuthorDao dao)
	{
		this.authorDao = dao;
	}
	
	@Override
	public void setRepresentation(ConsoleRepresentation representation) 
	{
		this.consoleRepresentation = representation;
	}
	
	@Override
	public void save() 
	{
		//First, user enters title and authors for new book.
		String inputDescription = "Enter information about new book.\nTitle : ";
		String title = consoleRepresentation.getNotEmptyStringInput(inputDescription);
		if (consoleRepresentation.isCancellingCommand(title))
			return;
		
		inputDescription = "Author (if more than one, just separete them by comma) : ";
		String authors = consoleRepresentation.getNotEmptyStringInput(inputDescription);
		if (consoleRepresentation.isCancellingCommand(authors))
			return;
		
		//Than program creates Book instance from users input and checks if identical book is already saved.
		Book newBook = constructNewBookFromInput(title, authors);
		
		if (isAlreadySaved(newBook))
		{
			consoleRepresentation.printLine(
					"Sorry, but your collection already contains " + newBook.toString());
			return;
		}
		
		// If there are already saved authors, than book will be saved without them and updated with those later
		Set<Author> savedAuthors = getSavedAuthors(newBook.getAuthors());
		savedAuthors.forEach(newBook::removeAuthor);
		
		bookDao.create(newBook);
		
		if (!savedAuthors.isEmpty())
		{
			savedAuthors.forEach(newBook::addAuthor);
			bookDao.update(newBook);
		}
		printResultOfOperation(true);
	}
	
	/**
	 * In order to avoid exception while trying to save existing authors
	 * @param authorsToSave
	 * @return		Set of Author objects, which already have representation in database
	 */
	private Set<Author> getSavedAuthors(Set<Author> authorsToSave)
	{
		Set<Author> savedAuthors = new HashSet<>();
		
		authorsToSave.forEach(author ->
		{
			Author possiblySaved = authorDao.readByName(author.getName());
			if (possiblySaved != null)
				savedAuthors.add(possiblySaved);
		});
		
		return savedAuthors;
	}
	
	/**
	 * Checks if the book with the same title and authors is already in database.
	 * @param book
	 * @return	True if the book has representation in database. False in another case.
	 */
	private boolean isAlreadySaved(Book book)
	{
		List<Book> booksWithSameTitle = bookDao.readByTitle(book.getTitle());
		
		if (booksWithSameTitle.isEmpty())
			return false;
		
		return booksWithSameTitle.contains(book);
	}
	
	private Book constructNewBookFromInput(String titleLine, String authorsLine)
	{
		Book newBook = new Book(titleLine.replaceAll(" +", " "));
		Set<Author> authors = getAuthorsFromLine(authorsLine);
		authors.forEach(newBook::addAuthor);
		
		return newBook;
	}
	
	/**
	 * Creates Set of Author object form the users input.
	 * @param line
	 * @return
	 */
	private Set<Author> getAuthorsFromLine(String line)
	{
		String[] lineAuthors = line.split(",");
		Set<Author> authors = new HashSet<>();
		
		for (String author : lineAuthors)
			authors.add(new Author(author.trim()));
		
		return authors;
	}
	
	@Override
	public void edit() 
	{
		String inputDescription = "Write a tittle of the book you want to edit : ";
		Book bookToEdit = selectSingleBookForEditingOrRemoving(inputDescription);
		if (bookToEdit == null)
			return;
		
		inputDescription = "Enter new name for \'" + bookToEdit.getTitle() + "\' : ";
		String newTitle = consoleRepresentation.getNotEmptyStringInput(inputDescription);
		
		if (consoleRepresentation.isCancellingCommand(newTitle))
			return;
		
		if (newTitle.equals(bookToEdit.getTitle()))
		{
			String message = "There is no use in changing name to the same name. ";
			message += "But good effort, I like that spirit.";
			consoleRepresentation.printLine(message);
			return;
		}
		
		bookToEdit.setTitle(newTitle);
		boolean isEdited = bookDao.update(bookToEdit);
		printResultOfOperation(isEdited);
	}

	@Override
	public void remove() 
	{
		String inputDescription = "Write a tittle of the book you want to remove from library : ";
		Book bookToDelete = selectSingleBookForEditingOrRemoving(inputDescription);
		if (bookToDelete == null)
			return;
		
		boolean isDeleted = bookDao.delete(bookToDelete);
		
		printResultOfOperation(isDeleted);
	}
	
	/**
	 * Informs about result of recent operation, performed by user.
	 * @param succeeded
	 */
	private void printResultOfOperation(boolean succeeded)
	{
		String operationResult = succeeded ? "Operation has been successfully completed." 
				: "Something gone wrong. Unable to complete operation.";
		consoleRepresentation.printLine(operationResult);
	}
	
	/**
	 * Method, created to repeating the same code in <code>edit</code> and <code>remove</code>
	 * @param inputDescription
	 * @return
	 */
	private Book selectSingleBookForEditingOrRemoving(String inputDescription)
	{
		String line = consoleRepresentation.getNotEmptyStringInput(inputDescription);
		
		if (consoleRepresentation.isCancellingCommand(line))
			return null;
		
		List<Book> searchedBooks = bookDao.readByTitle(line);
		int amountOfFoundBooks = searchedBooks.size();
		
		if (amountOfFoundBooks == 0)
		{
			consoleRepresentation.printLine("I cannot find any book by given title. Please, try again.");
			return null;
		}
		
		Book selected = null;
		int index = 0;
		
		if (amountOfFoundBooks > 1)
			index = selectConcreteBookIndex(searchedBooks);
		
		selected = searchedBooks.get(index);
		return selected;
	}
	
	private int selectConcreteBookIndex(List<Book> books)
	{
		consoleRepresentation.<Book>printEntitiesWithRespectiveIndexes(books);
		return consoleRepresentation.getIndexLimitedBy(books.size());
	}
	
	@Override
	public void showAll() 
	{
		List<Book> allPersistedBooks = bookDao.readAll();
		printListOfBooks(allPersistedBooks);
	}
	
	@Override
	public void showByTitle() 
	{
		String input = consoleRepresentation.getNotEmptyStringInput("Type down a title of the book(s) you want to find : ");
		
		if (consoleRepresentation.isCancellingCommand(input))
			return;
		
		List<Book> books = bookDao.readByTitle(input);
		printListOfBooks(books);
	}
	
	private void printListOfBooks(List<Book> books)
	{	
		if (books.isEmpty())
		{
			consoleRepresentation.printLine("I wasn't able to find any book.");
			return;
		}
		
		books.sort((book1, book2) -> book1.getTitle().compareTo(book2.getTitle()));
		books.forEach(book -> consoleRepresentation.printLine(book.toString()));
	}
}