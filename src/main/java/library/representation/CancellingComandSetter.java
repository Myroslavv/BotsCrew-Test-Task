package library.representation;

/**
 * This interface's purpose is to guarantee that
 * implementation is able to stop its flow when necessary.
 * @author Myroslav
 *
 */
public interface CancellingComandSetter
{
	/**
	 * Sets command value, which will tells program to stop caller of this method
	 * @param command
	 */
	void setCancellingCommand(String command);
}
