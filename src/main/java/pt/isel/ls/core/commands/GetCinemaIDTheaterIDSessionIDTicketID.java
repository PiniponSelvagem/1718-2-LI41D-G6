package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketIDView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetCinemaIDTheaterIDSessionIDTicketID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {


        //TODO:
        /*
            GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid} - returns the detailed information of the ticket, includes session information.
        */

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT tk.seat, tk.row,s.sid, s.Date,m.mid,t.tid,t.SeatsAvailable,t.Rows, t.Seats, t.Theater_Name,c.cid, m.Title, m.Release_Year ,m.Duration " +
                "FROM TICKET AS tk " +
                "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                "INNER JOIN THEATER AS t ON s.tid=t.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE tk.tkid=? AND tk.sid=?"
        );
        stmt.setString(1, cmdBuilder.getId(String.valueOf(TICKET_ID)));
        stmt.setString(2, cmdBuilder.getId(String.valueOf(SESSION_ID)));
        ResultSet rs = stmt.executeQuery();

        DataContainer data=new DataContainer(cmdBuilder.getHeader());
        int seat, sid, mid, tid, availableSeats, rows, seatsRow, cid, year, duration;
        String row;
        Timestamp date;
        String theaterName, title;

        if(rs.next()) {
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


            data.add(new Ticket(row.charAt(0), seat, new Session(sid, date, new Movie(mid, title, year, duration),
                    new Theater(tid, theaterName, rows, seatsRow, availableSeats, cid), cid))
            );
        }

        return new GetCinemaIDTheaterIDSessionIDTicketIDView(data);
    }
}
