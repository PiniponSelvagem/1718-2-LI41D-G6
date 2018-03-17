package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class PostCinemas implements Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO CINEMA VALUES (?, ?)");
        stmt.setString(1, cmdBuilder.getParameter((String.valueOf(NAME))));
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(CITY))));
        stmt.executeUpdate();

        return new PostView();
    }
}
