package pt.isel.ls.core.commands;

import pt.isel.ls.apps.http.HttpServer;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;

import java.sql.Connection;
import java.sql.SQLException;

public class Listen extends Command {
    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        try {
            new HttpServer(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSQLRequired() {
        return false;
    }
}
