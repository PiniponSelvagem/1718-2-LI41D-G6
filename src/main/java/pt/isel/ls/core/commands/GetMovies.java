package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetMoviesView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetMovies extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * from MOVIE");
        ResultSet rs = stmt.executeQuery();

        DataContainer dataContainer = new DataContainer(cmdBuilder.getHeader());

        while(rs.next()){
            dataContainer.add(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
        }

        return new GetMoviesView(dataContainer);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
