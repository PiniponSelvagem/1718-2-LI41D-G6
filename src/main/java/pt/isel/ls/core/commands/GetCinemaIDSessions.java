package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetCinemaIDSessions extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.sid, s.Date, m.Title, m.Duration, t.Theater_Name, st.seats, t.cid, t.tid, m.mid FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "INNER JOIN SEATS AS st ON s.sid=st.sid " +
                "WHERE cid=?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        ResultSet rs = stmt.executeQuery();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        int id, availableSeats, cid, tid, mid, duration;
        Timestamp dateTime;
        String theaterName, title;

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

            data.add(
                    new Session(id, dateTime,
                            new Movie(mid, title, NA, duration),
                            new Theater(tid, theaterName, NA, NA, availableSeats, cid),
                            cid
                    )
            );
        }

        return new GetCinemaIDSessionsView(data, Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))));
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
