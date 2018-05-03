package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class PostMovies extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO MOVIE VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, cmdBuilder.getParameter(TITLE.toString()));
        stmt.setInt(2, Integer.parseInt(cmdBuilder.getParameter(YEAR.toString())));
        stmt.setInt(3, Integer.parseInt(cmdBuilder.getParameter(DURATION.toString())));
        stmt.executeUpdate();

        int id = 0;
        ResultSet rs = stmt.getGeneratedKeys();
        if(rs.next()) id = rs.getInt(1);

        return new PostView<>("Movie ID = ", id);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
