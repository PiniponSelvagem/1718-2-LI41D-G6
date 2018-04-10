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
     * @throws CommandException
     */
    public CommandView execute(CommandBuilder cmdBuilder) throws CommandException {
        throw new CommandException("COMMAND NOT CORRECTLY IMPLEMENTED.");
    }

    /**
     * Accesses the Database and executes the SQL queries
     *
     * @param cmdBuilder
     * @param connection
     * @throws CommandException
     * @throws SQLException
     */
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        throw new CommandException("COMMAND NOT CORRECTLY IMPLEMENTED.");
    }
}
