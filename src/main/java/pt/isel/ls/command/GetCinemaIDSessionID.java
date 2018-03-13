package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;

public class GetCinemaIDSessionID implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        //return "SELECT * FROM CINEMA_SESSIONS AS s INNER JOIN CINEMA AS c WHERE c.cid="+
          //      cmdBuilder.popId()+" AND s.sid="+cmdBuilder.popId();
    }

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException {

    }
}
