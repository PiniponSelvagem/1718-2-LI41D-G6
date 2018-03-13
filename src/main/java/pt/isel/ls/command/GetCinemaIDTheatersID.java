package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;

public class GetCinemaIDTheatersID implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        //return "SELECT * FROM THEATER AS t INNER JOIN CINEMA AS c WHERE c.cid="+
          //      cmdBuilder.popId()+" AND t.tid="+cmdBuilder.popId();
    }

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException {

    }
}
