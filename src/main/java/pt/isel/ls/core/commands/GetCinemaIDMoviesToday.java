package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsView;
import pt.isel.ls.view.command.GetMoviesView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID;

public class GetCinemaIDMoviesToday extends Command{

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        Date date = new java.sql.Date( new java.util.Date().getTime());

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT m.mid,m.Title,m.Release_Year,m.Duration FROM CINEMA_SESSION AS s " +
                        "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                        "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                        "WHERE t.cid=? AND (CAST(s.Date AS DATE))=?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        stmt.setDate(2, date);
        ResultSet rs = stmt.executeQuery();

        DataContainer data =  new DataContainer(cmdBuilder.getHeader());
        int mid,year, duration;
        String title;

        while(rs.next()){
            mid = rs.getInt(1);
            title = rs.getString(2);
            year = rs.getInt(3);
            duration = rs.getInt(4);

            data.add(new Movie(mid, title, year, duration));
        }

        return new GetMoviesView(data);
    }
}
