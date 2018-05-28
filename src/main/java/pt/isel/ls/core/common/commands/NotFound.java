package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.ExceptionEnum.COMMAND__NOT_FOUND;

public class NotFound extends Command {

    @Override
    public String getMethodName() {
        return null;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder) throws CommandException {
        throw new CommandException(COMMAND__NOT_FOUND);
    }
}
