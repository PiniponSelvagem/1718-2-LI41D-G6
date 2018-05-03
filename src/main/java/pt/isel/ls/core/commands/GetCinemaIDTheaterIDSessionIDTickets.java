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
                "SELECT tk.row, tk.seat, s.Date, m.Title, m.Duration, t.Theater_Name, t.cid, t.tid, s.sid, m.mid " +
                "FROM TICKET AS tk " +
                "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                "INNER JOIN THEATER AS t ON s.tid=t.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE s.sid=?"
        );
        stmt.setString(1, cmdBuilder.getId(SESSION_ID.toString()));
        ResultSet rs = stmt.executeQuery();

        DataContainer data=new DataContainer(cmdBuilder.getHeader());
        int seat, sid=0, cid=NA, mid, tid, duration;
        String row;
        Timestamp date;
        String theaterName, title;

        while(rs.next()){
            row = rs.getString(1);
            seat = rs.getInt(2);
            date = rs.getTimestamp(3);
            title = rs.getString(4);
            duration = rs.getInt(5);
            theaterName = rs.getString(6);
            cid = rs.getInt(7);
            tid = rs.getInt(8);
            sid = rs.getInt(9);
            mid = rs.getInt(10);

            data.add(
                    new Ticket(row.charAt(0), seat,
                            new Session(sid, date,
                                    new Movie(mid, title, NA, duration),
                                    new Theater(tid, theaterName, NA, NA, NA, cid),
                                    cid
                            )
                    )
            );
        }

        return new GetCinemaIDTheaterIDSessionIDTicketsView(data, cid, sid);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
