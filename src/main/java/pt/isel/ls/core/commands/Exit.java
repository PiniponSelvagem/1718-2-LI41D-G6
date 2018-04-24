package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.ExitView;

import java.sql.Connection;

public class Exit extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection con) {
        return new ExitView();
    }

    @Override
    public boolean isSQLRequired() {
        return false;
    }
}
