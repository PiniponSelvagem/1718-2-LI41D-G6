package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;

import java.sql.Connection;

public class NotFound extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder) throws CommandNotFoundException {
        throw new CommandNotFoundException();
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandNotFoundException {
        throw new CommandNotFoundException();
    }
}
