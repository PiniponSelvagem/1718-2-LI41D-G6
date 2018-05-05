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

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.GET;
import static pt.isel.ls.core.strings.CommandEnum.MOVIES;

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
