package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.phase3_code.apps.http.FirstHttpServer;
import pt.isel.ls.phase3_code.apps.http.todo.ToDoApp;
import pt.isel.ls.view.command.CommandView;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.TEST;

public class Test extends Command {
    @Override
    public String getMethodName() {
        return TEST.toString();
    }

    @Override
    public String getPath() {
        return DIR_SEPARATOR.toString();
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        try {
            //FirstHttpServer.main(null);
            ToDoApp.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSQLRequired() {
        return false;
    }
}
