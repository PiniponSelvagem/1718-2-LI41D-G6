package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Command {

    /**
     * Execute internal core, without the need of SQL connection.
     *
     * @param cmdBuilder Command builder, aka context
     * @throws CommandException This should never happen, but in case this happens it allways throws CommandException
     *                          but the implementation of this method should only throw it if command was wrong or similar
     */
    public CommandView execute(CommandBuilder cmdBuilder) throws CommandException {
        throw new CommandException("COMMAND NOT CORRECTLY IMPLEMENTED.");
    }

    /**
     * Accesses the Database and executes the SQL queries
     *
     * @param cmdBuilder Command builder, aka context
     * @param connection SQL connection
     * @throws CommandException This should never happen, but in case this happens it allways throws CommandException
     *                          but the implementation of this method should only throw it if command was wrong or similar
     * @throws SQLException SQLException
     */
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        throw new CommandException("COMMAND NOT CORRECTLY IMPLEMENTED.");
    }
}
