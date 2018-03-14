package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public interface Command {

    /* !!! DISABLED ATM SINCE ITS NOT BEING USED !!!
    /**
     * @param cmdBuilder Command builder, aka context
     * @throws InvalidCommandParametersException
     * @throws CommandNotFoundException
     */
    //void execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException, CommandNotFoundException;

    /**
     * Accesses the Database and executes the SQL queries
     *
     * @param cmdBuilder
     * @param connection
     * @throws InvalidCommandParametersException
     * @throws CommandNotFoundException
     * @throws SQLException
     */
    void execute(CommandBuilder cmdBuilder, Connection connection)
            throws InvalidCommandParametersException, CommandNotFoundException, SQLException;

}
