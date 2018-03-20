package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.HelpView;

public class Help extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder) throws CommandNotFoundException {
        return new HelpView();
    }
}
