package library.representation.view;

import library.controller.BookController;

/**
 * Represents available actions(commands) user can perform,
 * using this class, to operate with books.
 * @author Myroslav
 *
 */
public class BookView extends AbstractView
{
	private final BookController bookController;
	
	public BookView(BookController controller)
	{
		this.bookController = controller;
		
		commands.put("Save new book", bookController::save);
		commands.put("Edit book", bookController::edit);
		commands.put("Remove book", bookController::remove);
		commands.put("Show all saved books", bookController::showAll);
		commands.put("Find books by title", bookController::showByTitle);
	}
}
