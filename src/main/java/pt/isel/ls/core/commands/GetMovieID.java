package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetMovieIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetMovieID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MOVIE WHERE mid = ?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(MOVIE_ID)));
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            return new InfoNotFoundView();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        data.add(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));

        return new GetMovieIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
