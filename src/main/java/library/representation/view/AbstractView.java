package library.representation.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class implements common to all View instances methods 
 * and defines the inner structure of possible actions to perform.
 * @author Myroslav
 *
 */
public abstract class AbstractView implements View
{
	/**
	 * Operations/actions/commands, which user would be able to perform
	 * using derivative classes.
	 */
	protected Map<String, Runnable> commands = new HashMap<>();
	
	@Override
	public void executeCommand(String command) 
	{
		commands.get(command).run();
	}

	
	@Override
	public List<String> getListOfCommands() 
	{
		Set<String> stringCommands = this.commands.keySet();
		return new ArrayList<String>(stringCommands);
	}
}
