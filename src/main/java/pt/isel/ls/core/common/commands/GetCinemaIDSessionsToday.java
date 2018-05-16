package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsDateIDView;
import pt.isel.ls.view.command.GetCinemaIDSessionsView;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_AVAILABLE_SEATS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMA;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SESSIONS;

public class GetCinemaIDSessionsToday extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL+DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+TODAY;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        Date date = new java.sql.Date(new java.util.Date().getTime());

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.sid, m.Title, m.Duration, t.Theater_Name, t.SeatsAvailable, s.Date, t.cid, t.tid, m.mid," +
                        " s.SeatsAvailable FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE cid=? AND (CAST(s.Date AS DATE))=?"
        );

        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setDate(2, date);
        ResultSet rs = stmt.executeQuery();
        LinkedList<Integer> as = new LinkedList<>();
        DataContainer data =  new DataContainer(cmdBuilder.getHeader());
        int id, availableSeats, cid, tid, mid, duration;
        Timestamp dateTime;
        String theaterName, title;

        LinkedList<Session> sessions = new LinkedList<>();
        while(rs.next()){
            id = rs.getInt(1);
            title = rs.getString(2);
            duration = rs.getInt(3);
            theaterName = rs.getString(4);
            availableSeats = rs.getInt(5);
            dateTime = rs.getTimestamp(6);
            cid = rs.getInt(7);
            tid = rs.getInt(8);
            mid = rs.getInt(9);
            as.add(rs.getInt(10));
            sessions.add( new Session(id, dateTime,
                            new Movie(mid, title, NA, duration),
                            new Theater(tid, theaterName, NA, NA, availableSeats, cid),
                            cid)
            );
        }
        data.add(D_AVAILABLE_SEATS, as);
        data.add(D_SESSIONS, sessions);
        data.add(D_CINEMA,cmdBuilder.getId(CINEMA_ID.toString()));
        return new GetCinemaIDSessionsDateIDView(
                data,
                Integer.parseInt(cmdBuilder.getId(CINEMA_ID.toString())),
                date
        );
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}

