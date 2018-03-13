package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;

import static pt.isel.ls.command.strings.CommandEnum.DURATION;
import static pt.isel.ls.command.strings.CommandEnum.TITLE;
import static pt.isel.ls.command.strings.CommandEnum.YEAR;

public class PostMovies implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException {
        /*return "INSERT INTO MOVIE VALUES ('"+
                cmdBuilder.getParameter(String.valueOf(TITLE))+"',"+
                cmdBuilder.getParameter(String.valueOf(YEAR))+","+
                cmdBuilder.getParameter(String.valueOf(DURATION))+");";
                */
    }

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException {

    }
}
