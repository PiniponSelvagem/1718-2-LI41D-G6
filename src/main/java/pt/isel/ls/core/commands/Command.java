package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Command {

    protected final static int NA = -1;

    /**
     * Executes the command and at the end returns CommandView.
     *
     * @param cmdBuilder Command builder, aka context
     * @param connection SQL connection
     * @throws CommandException This should never happen, but in case this happens it allways throws CommandException
     *                          but the implementation of this method should only throw it if command was wrong or similar
     * @throws SQLException SQLException
     */
    public abstract CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException;

    /**
     * @return Returns if this command requires a SQL connection for its execution.
     */
    public abstract boolean isSQLRequired();
}
