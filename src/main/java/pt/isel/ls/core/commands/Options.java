package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.OptionsView;

public class Options extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder) {
        return new OptionsView();
    }
}
