package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public interface Command {

    /**
     * Builds the SQL query string and returns it.
     *
     * @param cmdBuilder Command builder, aka contex
     * @throws InvalidCommandParametersException
     * @throws CommandNotFoundException
     */
    void execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException, CommandNotFoundException;
    void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException;

}
