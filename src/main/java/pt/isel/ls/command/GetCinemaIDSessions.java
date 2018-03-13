package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;

public class GetCinemaIDSessions implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        //return null;
    }

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException {

    }
}
