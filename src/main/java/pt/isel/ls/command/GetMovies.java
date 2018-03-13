package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;

public class GetMovies implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        //return "SELECT Title, Release_Year FROM MOVIE";
    }

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException {

    }
}
