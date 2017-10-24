package library.application;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import library.controller.BookController;
import library.controller.BookControllerImpl;
import library.dao.AuthorDao;
import library.dao.AuthorDaoImpl;
import library.dao.BookDao;
import library.dao.BookDaoImpl;
import library.database.HibernateConnector;
import library.database.HibernateMySqlConnector;
import library.model.Author;
import library.model.Book;
import library.representation.BasicEclipseRepresentation;
import library.representation.ConsoleRepresentation;
import library.representation.view.BookView;
import library.representation.view.View;

public class Application 
{
	public static void main(String[] args) 
	{
		//Avoiding annoying log in Eclipse (in my case).
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
		
		//Excuses why it takes so long.
		System.out.println("Your Personal book collection is opening, it might take a moment.");
		HibernateConnector connector = new HibernateMySqlConnector();
		try(SessionFactory sessionFactory = connector.getConnection())
		{
			//First, we define key components needed for our application to inject them.
			BookDao bookDao = new BookDaoImpl(sessionFactory);
			saveSomeBooks(bookDao);
			
			String commandEndingLine = "cancel";
			AuthorDao authorDao = new AuthorDaoImpl(sessionFactory);
			ConsoleRepresentation controllerRepresentation = new BasicEclipseRepresentation(commandEndingLine);
			
			BookController bookController = new BookControllerImpl(bookDao, authorDao, controllerRepresentation);
			View bookView = new BookView(bookController);
			
			//Second, we prepare basic values interact with user.
			String endingCommand = "exit";
			String greetingLine = "Welcome to your personal library. ";
			greetingLine += "Choose what you want to do by typing index of operation ";
			greetingLine += "and pressing \'Enter\' button.\n";
			greetingLine += "If you want to finish working with this program, just type \'";
			greetingLine += endingCommand + "\'.\n";
			greetingLine += "P.S.: It will work only in main menu. ";
			greetingLine += "If you've already started some operation just enter \'";
			greetingLine += commandEndingLine + "\' to get back to main menu.";
			
			ConsoleRepresentation applicationOutput = new BasicEclipseRepresentation(endingCommand);
			List<String> commands = bookView.getListOfCommands();
			int commandsAmount = commands.size();
			int index = -1;
			String command = "";
			
			//Lastly, running program until user shuts it down.
			while(true)
			{
				applicationOutput.cleanConsole();
				applicationOutput.printLine(greetingLine);
				
				applicationOutput.printEntitiesWithRespectiveIndexes(commands);
				index = applicationOutput.getIndexLimitedBy(commandsAmount);
				
				if (index == -1)
				{
					applicationOutput.printLine(
							"Thank you for using this program, it will shut down in a few seconds. Have a nice day :)");
					try 
					{
						Thread.sleep(5000);
					}
					catch(Exception ex) {}
					applicationOutput.cleanConsole();
					break;
				}
				
				command = commands.get(index);
				bookView.executeCommand(command);
				
				applicationOutput.waitBeforeProceeding();
			}
		}
		catch(HibernateException ex)
		{
			System.out.println("An error occured while opening collection. Terminating the program.");
		}
	}
	
	/**
	 * 
	 * @param dao	BookDao to insert some books 
	 * 				at the very start of program. 
	 */
	static void saveSomeBooks(BookDao dao)
	{
		Author arthur = new Author("Arthur Conan Doyle");
		Author stephen = new Author("Stephen Hawking");
		Author michio = new Author("Michio Kaku");
		Author mark = new Author("Mark Twain");
		//And this one for you, who created this task ;)
		Author theOneWhoDrinksTheTearsOfFans = new Author("George R. R. Martin");
		
		Book adventure = new Book("Adventure");
		adventure.addAuthor(arthur);
		dao.create(adventure);
		
		Book historyOfTime = new Book("History of time");
		historyOfTime.addAuthor(stephen);
		dao.create(historyOfTime);
		
		Book hiperSpace = new Book("Hiperspace");
		hiperSpace.addAuthor(michio);
		dao.create(hiperSpace);
		
		Book theoryOfEverything = new Book("THEORY OF EVERYTHING");
		dao.create(theoryOfEverything);
		theoryOfEverything.addAuthor(stephen);
		theoryOfEverything.addAuthor(michio);
		dao.update(theoryOfEverything);
		
		Book anotherAventure = new Book("Adventure");
		anotherAventure.addAuthor(mark);
		dao.create(anotherAventure);
		
		Book iHopeBetterThanSixthAndSeventhSeasons = new Book("The World of Ice & Fire");
		iHopeBetterThanSixthAndSeventhSeasons.addAuthor(theOneWhoDrinksTheTearsOfFans);
		dao.create(iHopeBetterThanSixthAndSeventhSeasons);
	}
}
