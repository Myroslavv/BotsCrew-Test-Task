package library.representation;

import java.util.Collection;

/**
 * Basic strategy of interaction between user and program.
 * @author Myroslav
 *
 */
public interface ConsoleRepresentation extends CancellingComandSetter
{
	/**
	 * Creates pause, until user decides to continue.
	 */
	void waitBeforeProceeding();
	
	/**
	 * 
	 * @param description	Line, printed to explain what is expected from user.
	 * @return				Not empty string value with no white spaces at its beginning and ending.
	 */
	String getNotEmptyStringInput(String description);
	
	/**
	 * Prints to the screen/console list of elements with corresponding indexes.
	 * <p>The output looks like this : "{index} : {collection element}".
	 * </p>
	 * @param collection	Collection of elements, which are to be printed (implementation of java.util.Collection).
	 * @param <T>			Type of objects, contained in collection.
	 */
	<T> void printEntitiesWithRespectiveIndexes(Collection<T> collection);
	
	/**
	 * 
	 * @param limit		The upper bound of possible positive valid results.
	 * @return			-1 in case of terminating the caller process. Number between [0, limit) in other cases.
	 */
	int getIndexLimitedBy(int limit);
	
	/**
	 * Checks if given line equals to reserved by program input, which stops caller execution.
	 * @param line	
	 * @return		true if line defined as terminating command, false in other cases.
	 */
	boolean isCancellingCommand(String line);
	
	/**
	 * Prints a String.
	 * NOTE: if you want to print in current line and start from next, you need to add \n in the end of passed string value.
	 * @param message
	 */
	void print(String message);
	
	/**
	 * Prints a String and then terminate the line. 
	 * @param message
	 */
	void printLine(String message);
	
	/**
	 * Cleans recent output.
	 */
	void cleanConsole();
}
