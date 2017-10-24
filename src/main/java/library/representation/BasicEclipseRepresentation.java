package library.representation;

import static java.lang.System.in;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * This class implements basic OI interaction between user and 
 * program.
 * 
 * <p>It's specified for use in Eclipse IDE.
 * But it might work good in IntelliJ IDEA too.
 * </p>
 * @author Myroslav
 *
 */
public class BasicEclipseRepresentation implements ConsoleRepresentation
{
	private String cancelCommand;

	public BasicEclipseRepresentation(String cancelCommand) 
	{
		this.cancelCommand = cancelCommand;
	}
	
	@Override
	public void waitBeforeProceeding()
	{
		out.print("Press \'Enter\' to continue...");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try
		{
			reader.readLine();
		}
		catch(Exception ex)
		{
		}
	}

	@Override
	public String getNotEmptyStringInput(String inputDescription)
	{
		String input = "";
		boolean inputIsInvalid = true;
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		while(inputIsInvalid)
		{
			out.print(inputDescription);
			try
			{
				input = reader.readLine().trim().replaceAll(" +", " ");
				
				if (input.isEmpty())
					out.println("Please, type something.");
				else
					inputIsInvalid = false;
			}
			catch(IOException ioEx)
			{
				out.println("An error occured. Please try again.");
			}
			catch(Exception ex)
			{
				out.println("An error occured. Cancelling operation.");
				return cancelCommand;
			}
		}
		
		return input;
	}

	@Override
	public <T> void printEntitiesWithRespectiveIndexes(Collection<T> collection) 
	{
		int i = 0;
		for(T element : collection)
			out.printf("%d : %s\n", i++, element.toString());
	}

	@Override
	public int getIndexLimitedBy(int limit) 
	{	
		String input = "";
		int index = -1;
		while(true)
		{
			try
			{
				input = getNotEmptyStringInput("Index : ").trim();
				
				if(isCancellingCommand(input))
					return -1;
				
				index = Integer.parseInt(input);
				if (index >= 0 && index < limit)
					break;
			}
			catch(Exception ex) 
			{
				out.println("An error occured,  please try again.");
			}
		}
		
		return index;
	}

	@Override
	public void setCancellingCommand(String command) 
	{
		this.cancelCommand = command;
	}

	@Override
	public boolean isCancellingCommand(String line) 
	{
		return line.toLowerCase().equals(cancelCommand);
	}

	@Override
	public void print(String message) 
	{
		System.out.print(message);
	}

	@Override
	public void printLine(String message) 
	{
		this.print(message);
		out.println();
	}

	@Override
	public void cleanConsole() 
	{
		for (int i = 0; i < 50; i++)
			out.println();
	}
	
	
}
