package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetMoviesView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetMovies implements Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * from MOVIE");
        ResultSet rs = stmt.executeQuery();

        GetMoviesView moviesView = new GetMoviesView();

        while(rs.next()){
            moviesView.add(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
        }

        return moviesView;
    }
}
