package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

public interface Command {
    
    String execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException, CommandNotFoundException;
}
