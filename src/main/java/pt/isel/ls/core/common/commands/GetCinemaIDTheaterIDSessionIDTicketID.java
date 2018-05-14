package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SESSION;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_TICKET;

public class GetCinemaIDTheaterIDSessionIDTicketID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX
                +DIR_SEPARATOR+TICKETS+DIR_SEPARATOR+ID_PREFIX+TICKET_ID+ID_SUFFIX;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        Ticket ticket;
        Session session;

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT tk.row, tk.seat, s.Date, m.Title, m.Duration, t.Theater_Name, c.cid, t.tid, s.sid, m.mid, t.Rows, t.Seats " +
                "FROM TICKET AS tk " +
                "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                "INNER JOIN THEATER AS t ON s.tid=t.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE tk.tkid=? AND tk.sid=?"
        );
        stmt.setString(1, cmdBuilder.getId(TICKET_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(SESSION_ID.toString()));
        ResultSet rs = stmt.executeQuery();

        DataContainer data=new DataContainer(cmdBuilder.getHeader());
        int seat, sid, tid, cid, mid, duration;
        String row;
        Timestamp date;
        String theaterName, title;

        if (!rs.next())
            return new InfoNotFoundView(data);

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
        session=new Session(sid, date,
                new Movie(mid, title, NA, duration),
                new Theater(tid, theaterName,rs.getInt(11), rs.getInt(12), NA, cid), cid);
        ticket=new Ticket(row.charAt(0), seat, session);
        data.add(D_SESSION,session);
        data.add(D_TICKET,ticket);

        return new GetCinemaIDTheaterIDSessionIDTicketIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
