package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetMovieIDView;

import java.sql.*;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class GetMovieID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MOVIE WHERE mid = ?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(MOVIE_ID)));
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            return null;

        return new GetMovieIDView(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
    }
}
