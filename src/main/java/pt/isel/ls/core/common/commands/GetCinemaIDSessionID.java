package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;


public class GetCinemaIDSessionID extends Command {

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
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.sid, s.Date, m.Title, m.Duration, t.Theater_Name, st.seats, t.cid, t.tid, m.mid FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "INNER JOIN SEATS AS st ON s.sid=st.sid " +
                "WHERE cid=? AND s.sid=?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(SESSION_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            return new InfoNotFoundView();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        int id, availableSeats, cid, tid, mid, duration;
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

        data.add(
                new Session(id, dateTime,
                        new Movie(mid, title, NA, duration),
                        new Theater(tid, theaterName, NA, NA, availableSeats, cid),
                        cid
                )
        );

        return new GetCinemaIDSessionIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
