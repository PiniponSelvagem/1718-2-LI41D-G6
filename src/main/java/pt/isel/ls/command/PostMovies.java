package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class PostMovies implements Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO MOVIE VALUES (?, ?, ?)");
        stmt.setString(1, cmdBuilder.getParameter((String.valueOf(TITLE))));
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(YEAR))));
        stmt.setString(3, cmdBuilder.getParameter((String.valueOf(DURATION))));
        stmt.execute();

        return new PostView();
    }
}
