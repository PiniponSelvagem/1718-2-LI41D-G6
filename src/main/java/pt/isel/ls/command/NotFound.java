package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;

public class NotFound implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) throws CommandNotFoundException {
        throw new CommandNotFoundException();
    }

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException {
        throw new CommandNotFoundException();
    }
}
