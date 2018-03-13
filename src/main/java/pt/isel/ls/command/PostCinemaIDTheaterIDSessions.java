package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;

import static pt.isel.ls.command.strings.CommandEnum.DATE;
import static pt.isel.ls.command.strings.CommandEnum.MOVIES_ID;
import static pt.isel.ls.command.strings.CommandEnum.THEATERS_ID;

public class PostCinemaIDTheaterIDSessions implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException {
        /*return "INSERT INTO CINEMA_SESSIONS VALUES ("+
                cmdBuilder.getParameter(String.valueOf(DATE))+","+
                cmdBuilder.getParameter(String.valueOf(MOVIES_ID))+","+
                cmdBuilder.getParameter(String.valueOf(THEATERS_ID))+");";
                */
    }

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException {

    }
}
