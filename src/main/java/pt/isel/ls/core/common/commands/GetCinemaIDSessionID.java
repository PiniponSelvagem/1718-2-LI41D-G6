package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


public class GetCinemaIDSessionID extends Command {
    // ligação com theatersID (link no nome), sessionsToday (link in today's sessions), ticketsID (link in list of tickets)

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        Session session;
        Theater theater;
        LinkedList<String> ticketIDs= new LinkedList<String>();

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.sid, s.Date, m.Title, m.Duration, t.Theater_Name, s.SeatsAvailable, t.cid, t.tid, m.mid, t.Rows,t.Seats, s.SeatsAvailable FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE cid=? AND s.sid=?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(SESSION_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());

        if (!rs.next())
            return new InfoNotFoundView(data);

        int id, availableSeats, cid, tid, mid, duration, SeatsAvailable;
        Timestamp dateTime;
        String theaterName, title;

        id = rs.getInt(1);
        dateTime = rs.getTimestamp(2);
        title = rs.getString(3);
        duration = rs.getInt(4);
        theaterName = rs.getString(5);
        availableSeats = rs.getInt(6);
        cid = rs.getInt(7);
        tid = rs.getInt(8);
        mid = rs.getInt(9);
        SeatsAvailable=rs.getInt(12);
        theater= new Theater(tid, theaterName, rs.getInt(10), rs.getInt(11), availableSeats, cid);
        session= new Session(id, dateTime,new Movie(mid, title, NA, duration),theater, cid);
        stmt = connection.prepareStatement(
                "SELECT * FROM TICKET AS tk WHERE tk.sid=?");
        stmt.setInt(1, id);
        rs = stmt.executeQuery();
        while(rs.next()) ticketIDs.add(""+rs.getString(3).charAt(0)+rs.getInt(2));
        data.add(D_AVAILABLE_SEATS,SeatsAvailable);
        data.add(D_THEATER, theater);
        data.add(D_SESSION, session);
        data.add(D_TICKETS, ticketIDs);

        return new GetCinemaIDSessionIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
