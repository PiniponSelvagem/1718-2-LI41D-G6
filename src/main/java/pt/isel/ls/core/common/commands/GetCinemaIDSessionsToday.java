package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsDateIDView;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

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
                "SELECT s.sid, m.Title, m.Duration, t.Theater_Name, t.SeatsAvailable, s.Date, t.tid, m.mid," +
                        " s.SeatsAvailable, c.cid, c.Name, c.City FROM CINEMA_SESSION AS s " +
                        "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                        "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                        "INNER JOIN CINEMA AS c ON c.cid = t.cid " +
                        "WHERE c.cid=? AND (CAST(s.Date AS DATE))=?"
        );

        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setDate(2, date);
        ResultSet rs = stmt.executeQuery();
        DataContainer data =  new DataContainer(cmdBuilder.getHeader());
        int id, seats, cid = Integer.parseInt(cmdBuilder.getId(CINEMA_ID.toString())), tid, mid, duration, availableSeats;
        Timestamp dateTime;
        String theaterName, title;
        Cinema cinema = null;
        LinkedList<Session> sessions = new LinkedList<>();
        while(rs.next()){
            id = rs.getInt(1);
            title = rs.getString(2);
            duration = rs.getInt(3);
            theaterName = rs.getString(4);
            seats = rs.getInt(5);
            dateTime = rs.getTimestamp(6);
            tid = rs.getInt(7);
            mid = rs.getInt(8);
            availableSeats = rs.getInt(9);

            if (cinema==null) {
                cid = rs.getInt(10);
                cinema = new Cinema(rs.getInt(10), rs.getString(11), rs.getString(12));
                data.add(D_CINEMA, cinema);
            }
            sessions.add(new Session(id, availableSeats, dateTime,
                            new Movie(mid, title, NA, duration),
                            new Theater(tid, theaterName, NA, NA, seats, cid),
                            cid)
            );
        }
        data.add(D_SESSIONS, sessions);
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

