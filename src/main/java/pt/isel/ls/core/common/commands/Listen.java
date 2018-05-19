package pt.isel.ls.core.common.commands;

import pt.isel.ls.apps.http_server.HttpServer;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;

import java.net.BindException;
import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.LISTEN;
import static pt.isel.ls.core.strings.CommandEnum.SERVER_PORT;
import static pt.isel.ls.core.strings.ExceptionEnum.SERVER_PORT_INVALID_FORMAT;

public class Listen extends Command {

    @Override
    public String getMethodName() {
        return LISTEN.toString();
    }

    @Override
    public String getPath() {
        return DIR_SEPARATOR.toString();
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        int port;
        try {
            port = Integer.parseInt(cmdBuilder.getParameter(SERVER_PORT.toString()));
            new HttpServer(port);
        }
        catch (NumberFormatException e){
            throw new CommandException(SERVER_PORT_INVALID_FORMAT);
        }
        return null;
    }

    @Override
    public boolean isSQLRequired() {
        return false;
    }
}