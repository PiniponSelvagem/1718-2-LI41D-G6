package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDMoviesView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetCinemaIDMoviesToday extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        Date date = new java.sql.Date( new java.util.Date().getTime());

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT m.mid, c.cid, m.Title, m.Release_Year, m.Duration " +
                        "FROM MOVIE AS m INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                        "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                        "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                        "WHERE c.cid=? AND (CAST(s.Date AS DATE))=?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        stmt.setDate(2, date);
        ResultSet rs = stmt.executeQuery();

        DataContainer data =  new DataContainer(cmdBuilder.getHeader());
        int mid, cid = 0, year, duration;
        String title;

        while(rs.next()){
            mid = rs.getInt(1);
            cid = rs.getInt(2);
            title = rs.getString(3);
            year = rs.getInt(4);
            duration = rs.getInt(5);

            data.add(new Movie(mid, title, year, duration));
        }

        return new GetCinemaIDMoviesView(data,
                cid,
                date
        );
    }
}
