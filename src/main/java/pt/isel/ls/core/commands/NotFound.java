package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;

import java.sql.Connection;

import static pt.isel.ls.core.strings.ExceptionEnum.COMMAND__NOT_FOUND;

public class NotFound extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException {
        throw new CommandException(COMMAND__NOT_FOUND);
    }

    @Override
    public boolean isSQLRequired() {
        return false;
    }
}
