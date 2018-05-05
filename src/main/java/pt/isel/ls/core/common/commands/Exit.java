package pt.isel.ls.core.common.commands;

import pt.isel.ls.Main;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.ExitView;

import java.sql.Connection;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.EXIT;

public class Exit extends Command {

    @Override
    public String getMethodName() {
        return EXIT.toString();
    }

    @Override
    public String getPath() {
        return DIR_SEPARATOR.toString();
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection con) {
        Main.close();
        return new ExitView();
    }

    @Override
    public boolean isSQLRequired() {
        return false;
    }
}
