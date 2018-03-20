package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.*;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class PostMovies extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO MOVIE VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, cmdBuilder.getParameter((String.valueOf(TITLE))));
        stmt.setInt(2, Integer.parseInt(cmdBuilder.getParameter((String.valueOf(YEAR)))));
        stmt.setInt(3, Integer.parseInt(cmdBuilder.getParameter((String.valueOf(DURATION)))));
        stmt.executeUpdate();

        int id = 0;
        ResultSet rs = stmt.getGeneratedKeys();
        if(rs.next()) id = rs.getInt(1);

        return new PostView("Movie ID = ", id);
    }
}
