package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketsView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.SESSION_ID;

public class GetCinemaIDTheaterIDSessionIDTickets extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT tk.seat, tk.row,s.sid, s.Date,m.mid,t.tid,t.SeatsAvailable,t.Rows, t.Seats, t.Theater_Name,c.cid, m.Title, m.Release_Year ,m.Duration " +
                "FROM TICKET AS tk " +
                "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                "INNER JOIN THEATER AS t ON s.tid=t.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE s.sid=?"
        );
        stmt.setString(1, cmdBuilder.getId(String.valueOf(SESSION_ID)));
        ResultSet rs = stmt.executeQuery();

        DataContainer data=new DataContainer(cmdBuilder.getHeader());
        int seat, sid=0, mid, tid, availableSeats, rows, seatsRow, cid=0, year, duration;
        String row;
        Timestamp date;
        String theaterName, title;

        while(rs.next()){
            seat = rs.getInt(1);
            row = rs.getString(2);
            sid = rs.getInt(3);
            date = rs.getTimestamp(4);
            mid = rs.getInt(5);
            tid = rs.getInt(6);
            availableSeats = rs.getInt(7);
            rows = rs.getInt(8);
            seatsRow = rs.getInt(9);
            theaterName = rs.getString(10);
            cid = rs.getInt(11);
            title = rs.getString(12);
            year = rs.getInt(13);
            duration = rs.getInt(14);

            data.add(new Ticket(row.charAt(0),seat,new Session(sid, date, new Movie(mid, title, year, duration),
                    new Theater(tid, theaterName, rows, seatsRow, availableSeats, cid), cid))
            );
        }

        return new GetCinemaIDTheaterIDSessionIDTicketsView(data, cid, sid);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
