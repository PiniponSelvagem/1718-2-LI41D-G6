package pt.isel.ls.core.common.commands;

import pt.isel.ls.apps.http_server.HttpServer;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

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
    public DataContainer execute(CommandBuilder cmdBuilder) throws ParameterException {
        int port;
        try {
            port = Integer.parseInt(cmdBuilder.getParameter(SERVER_PORT));
            new HttpServer(port);
        }
        catch (NumberFormatException e){
            throw new ParameterException(SERVER_PORT_INVALID_FORMAT);
        }
        return new DataContainer(this.getClass().getSimpleName());
    }
}