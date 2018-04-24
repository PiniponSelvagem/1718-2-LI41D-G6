package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetMoviesView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID;

public class GetCinemaIDMoviesToday extends Command{

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        Date date = new java.sql.Date( new java.util.Date().getTime());

        PreparedStatement stmt = connection.prepareStatement("select * from MOVIE " +
                                    "inner join CINEMA_SESSION as s ON s.mid = MOVIE.mid " +
                                    "inner join THEATER as t ON t.tid = s.tid " +
                                    "inner join CINEMA as c ON c.cid = t.cid " +
                                    "where c.cid=? AND (CAST(s.Date AS DATE))=?"
        );
        stmt.setString(1, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        stmt.setDate(2, date);
        ResultSet rs = stmt.executeQuery();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        int mid, releaseYear, duration, cid = 0;
        String tname = "";
        String title="";

        while(rs.next()){
            mid = rs.getInt(1);
            title = rs.getString(2);
            releaseYear = rs.getInt(3);
            duration = rs.getInt(4);
            cid = rs.getInt(15);
            tname = rs.getString(13);

            data.add(new Movie(mid, title, releaseYear, duration));
        }
        return new GetMoviesView(data, cid, tname);
    }
}
