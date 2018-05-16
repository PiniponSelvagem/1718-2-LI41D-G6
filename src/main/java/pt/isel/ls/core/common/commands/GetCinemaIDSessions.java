package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsView;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SESSIONS;

public class GetCinemaIDSessions extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+SESSIONS;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.sid, s.Date, m.Title, m.Duration, t.Theater_Name, s.SeatsAvailable, t.cid, t.tid, m.mid FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE cid=?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        ResultSet rs = stmt.executeQuery();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        int id, availableSeats, cid, tid, mid, duration;
        Timestamp dateTime;
        String theaterName, title;

        LinkedList<Session> sessions = new LinkedList<>();
        while(rs.next()){
            id = rs.getInt(1);
            dateTime = rs.getTimestamp(2);
            title = rs.getString(3);
            duration = rs.getInt(4);
            theaterName = rs.getString(5);
            availableSeats = rs.getInt(6);
            cid = rs.getInt(7);
            tid = rs.getInt(8);
            mid = rs.getInt(9);

            sessions.add(
                    new Session(id, availableSeats, dateTime,
                            new Movie(mid, title, NA, duration),
                            new Theater(tid, theaterName, NA, NA, NA, cid),
                            cid
                    )
            );
        }
        data.add(D_SESSIONS, sessions);

        return new GetCinemaIDSessionsView(data, Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))));
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
