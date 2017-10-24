package library.controller;

import library.representation.ConsoleRepresentation;

/**
 * Defines method to make different possible strategies of input/output.
 * @author Myroslav
 *
 */
public interface ControllerWithRepresentation 
{
	void setRepresentation(ConsoleRepresentation representation);
}
