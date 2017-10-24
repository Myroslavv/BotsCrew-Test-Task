package library.representation.view;

import java.util.List;

/**
 * Represents container with available commands. 
 * Each non-abstract implementation is supposed 
 * to define it's controller and operations.
 * @author Myroslav
 *
 */
public interface View 
{
	/**
	 * Executes action, defined respectively to given command
	 */
	void executeCommand(String command);
	
	/**
	 * Returns defensive copy of commands, stored in View.
	 * @return
	 */
	List<String> getListOfCommands();
}
