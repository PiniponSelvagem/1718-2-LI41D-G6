package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionsView;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SESSIONS;

public class GetCinemaIDTheaterIDSessions extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX+DIR_SEPARATOR+SESSIONS;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.sid, s.Date, m.Title, m.Duration, s.SeatsAvailable, t.cid, t.tid, m.mid FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE cid=? AND t.tid=?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(THEATER_ID.toString()));
        ResultSet rs = stmt.executeQuery();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        int id, tid, availableSeats, cid, mid, duration;
        Timestamp dateSession;
        String title;

        LinkedList<Session> sessions = new LinkedList<>();
        while(rs.next()){
            id = rs.getInt(1);
            dateSession = rs.getTimestamp(2);
            title = rs.getString(3);
            duration = rs.getInt(4);
            availableSeats = rs.getInt(5);
            cid = rs.getInt(6);
            tid = rs.getInt(7);
            mid = rs.getInt(8);

            sessions.add(
                    new Session(id, dateSession,
                            new Movie(mid, title, NA, duration),
                            new Theater(tid, null, NA, NA, availableSeats, cid),
                            cid
                    )
            );
        }
        data.add(D_SESSIONS, sessions);

        return new GetCinemaIDTheaterIDSessionsView(
                data,
                Integer.parseInt(cmdBuilder.getId(CINEMA_ID.toString())),
                Integer.parseInt(cmdBuilder.getId(THEATER_ID.toString()))
        );
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
