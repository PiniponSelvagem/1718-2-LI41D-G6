package pt.isel.ls.core.common.commands;

import pt.isel.ls.apps.http.HttpServer;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.LISTEN;
import static pt.isel.ls.core.strings.CommandEnum.SERVER_PORT;
import static pt.isel.ls.core.strings.ExceptionEnum.SERVER_PORT_INVALID_FORMAT;

public class Listen extends Command {
    private static final int PORT = 8080;

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
        try {
            int port;
            try {
                port = Integer.parseInt(cmdBuilder.getParameter(SERVER_PORT.toString()));
            }
            catch (NumberFormatException e){
                //port = PORT;
                throw new CommandException(String.format(SERVER_PORT_INVALID_FORMAT.toString(), PORT));
            }
            new HttpServer(port);
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
