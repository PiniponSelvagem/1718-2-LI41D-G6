package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

public interface Command {

    /**
     * Builds the SQL query string and returns it.
     *
     * @param cmdBuilder Command builder, aka context
     * @return SQL query string ready to be send.
     * @throws InvalidCommandParametersException
     * @throws CommandNotFoundException
     */
    String execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException, CommandNotFoundException;
}
