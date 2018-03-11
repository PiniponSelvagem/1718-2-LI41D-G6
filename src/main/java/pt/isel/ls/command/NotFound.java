package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.utils.CommandBuilder;

public class NotFound implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) throws CommandNotFoundException {
        throw new CommandNotFoundException();
    }
}
