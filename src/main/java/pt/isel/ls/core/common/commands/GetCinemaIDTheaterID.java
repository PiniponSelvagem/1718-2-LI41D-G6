package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+THEATER_ID_FULL;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT t.tid, t.Theater_Name, t.SeatsAvailable, c.cid, c.Name, c.City, t.Rows, t.Seats " +
                "FROM THEATER AS t " +
                "INNER JOIN CINEMA AS c ON c.cid=? " +
                "WHERE t.tid=?"
        );
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(THEATER_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());

        if (!rs.next())
            return new InfoNotFoundView(data);

        int cid = rs.getInt(4);
        data.add(D_CINEMA, new Cinema(cid, rs.getString(5), rs.getString(6)));
        data.add(D_THEATER, new Theater(rs.getInt(1), rs.getString(2), rs.getInt(7), rs.getInt(8), rs.getInt(3), cid));




        stmt = connection.prepareStatement(
                "SELECT s.sid, s.Date, s.SeatsAvailable, m.mid, m.Title, m.Duration, m.Release_Year " +
                "FROM CINEMA_SESSION AS s " +
                "INNER JOIN CINEMA AS c ON c.cid=? " +
                "INNER JOIN THEATER AS t ON t.tid=? " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE t.tid=s.tid"
        );
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(THEATER_ID.toString()));
        rs = stmt.executeQuery();
        LinkedList<Session> sessions = new LinkedList<>();

        int sid, mid, availableSeats, duration, year;
        Timestamp dateTime;
        String title;

        while(rs.next()) {
            sid = rs.getInt(1);
            dateTime = rs.getTimestamp(2);
            availableSeats = rs.getInt(3);
            mid = rs.getInt(4);
            title = rs.getString(5);
            duration = rs.getInt(6);
            year = rs.getInt(7);

            sessions.add(
                    new Session(sid, availableSeats, dateTime,
                            new Movie(mid, title, year, duration),
                            null,
                            cid)
            );
        }
        data.add(D_SESSIONS, sessions);

        return new GetCinemaIDTheaterIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
