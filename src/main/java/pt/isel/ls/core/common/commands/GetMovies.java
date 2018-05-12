package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetMoviesView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.GET;
import static pt.isel.ls.core.strings.CommandEnum.MOVIES;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;

public class GetMovies extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * from MOVIE");
        ResultSet rs = stmt.executeQuery();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        LinkedList<Movie> movies = new LinkedList<>();
        while(rs.next()){
            movies.add(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
        }
        data.add(D_MOVIES, movies);

        return new GetMoviesView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
