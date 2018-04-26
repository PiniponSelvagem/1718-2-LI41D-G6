package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.TestView;

import java.sql.Connection;

import static pt.isel.ls.core.strings.CommandEnum.NAME;

public class Test extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection con) throws CommandException {

        DataContainer data = new DataContainer(cmdBuilder.getHeader());

        int id = 666;
        String name = cmdBuilder.getParameter(String.valueOf(NAME));

        data.add(new Cinema(id, name, null));

        return new TestView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return false;
    }
}
