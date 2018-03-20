package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Command {

    /**
     * Execute internal command, without the need of SQL connection.
     *
     * @param cmdBuilder Command builder, aka context
     * @throws InvalidCommandParametersException
     * @throws CommandNotFoundException
     */
    public CommandView execute(CommandBuilder cmdBuilder)
            throws InvalidCommandParametersException, CommandNotFoundException {
        throw new CommandNotFoundException();
    }

    /**
     * Accesses the Database and executes the SQL queries
     *
     * @param cmdBuilder
     * @param connection
     * @throws InvalidCommandParametersException
     * @throws CommandNotFoundException
     * @throws SQLException
     */
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection)
            throws InvalidCommandParametersException, CommandNotFoundException, SQLException {
        throw new CommandNotFoundException();
    }

}
