package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.*;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class PostCinemas implements Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO CINEMA VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, cmdBuilder.getParameter((String.valueOf(NAME))));
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(CITY))));
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        int id = 0;
        if(rs.next()) id = rs.getInt(1);


        return new PostView("Cinema ID: ", id);
    }
}
