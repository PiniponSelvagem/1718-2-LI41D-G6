package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsDateIDView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID;
import static pt.isel.ls.core.strings.CommandEnum.DATE_ID;

public class GetCinemaIDSessionsDateID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        //TODO:
        /*
           GET /cinemas/{cid}/sessions/date/{d} - return a list with the sessions in cinema cid in the day of the year d.
        */
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.sid, s.Date,m.mid,t.tid,t.SeatsAvailable,t.Rows, t.Seats, t.Theater_Name,c.cid, m.Title, m.Release_Year ,m.Duration " +
                "FROM MOVIE AS m INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid "+
                "INNER JOIN THEATER AS t ON t.tid=s.tid "+
                "INNER JOIN CINEMA AS c ON t.cid=c.cid "+
                "WHERE (CONVERT(s.Date, DATE))=? AND c.cid=?"
        );
        stmt.setString(1, cmdBuilder.getId(String.valueOf(DATE_ID)));
        stmt.setString(2, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        ResultSet rs = stmt.executeQuery();

        DataContainer data=new DataContainer(cmdBuilder.getHeader());
        int sid, mid, tid, availableSeats, rows, seatsRow, cid, year, duration;
        Timestamp date=null;
        String theaterName, title;

        while(rs.next()){
            sid = rs.getInt(1);
            date = rs.getTimestamp(2);
            mid = rs.getInt(3);
            tid = rs.getInt(4);
            availableSeats = rs.getInt(5);
            rows = rs.getInt(6);
            seatsRow = rs.getInt(7);
            theaterName = rs.getString(8);
            cid = rs.getInt(9);
            title = rs.getString(10);
            year = rs.getInt(11);
            duration = rs.getInt(12);

            data.add(new Session(sid, date, new Movie(mid, title, year, duration),
                    new Theater(tid, theaterName, rows, seatsRow, availableSeats, cid), cid));
        }

        return new GetCinemaIDSessionsDateIDView(
                data,
                Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))),
                date
        );
    }
}
