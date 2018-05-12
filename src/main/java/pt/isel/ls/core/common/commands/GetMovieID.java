package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetMovieIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIE;

public class GetMovieID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+ID_PREFIX+MOVIE_ID+ID_SUFFIX;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MOVIE WHERE mid = ?");
        stmt.setString(1, cmdBuilder.getId(MOVIE_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        if (!rs.next())
            return new InfoNotFoundView(data);

        data.add(D_MOVIE, new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));

        return new GetMovieIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
