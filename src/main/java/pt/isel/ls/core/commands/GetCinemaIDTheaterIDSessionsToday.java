package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionsView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetCinemaIDTheaterIDSessionsToday extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        Date date = new java.sql.Date( new java.util.Date().getTime());

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.sid, s.Date, m.Title, m.Duration, st.seats, t.cid, t.tid, m.mid FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "INNER JOIN SEATS AS st ON st.sid=s.sid " +
                "WHERE cid=? AND t.tid=? AND (CAST(s.Date AS DATE))=?"
        );
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(THEATER_ID.toString()));
        stmt.setDate(3, date);
        ResultSet rs = stmt.executeQuery();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        int id, mid, tid, availableSeats, cid, duration;
        Timestamp dateSession;
        String title;

        while(rs.next()){
            id = rs.getInt(1);
            dateSession = rs.getTimestamp(2);
            title = rs.getString(3);
            duration = rs.getInt(4);
            availableSeats = rs.getInt(5);
            cid = rs.getInt(6);
            tid = rs.getInt(7);
            mid = rs.getInt(8);

            data.add(
                    new Session(id, dateSession,
                            new Movie(mid, title, NA, duration),
                            new Theater(tid, null, NA, NA, availableSeats, cid),
                            cid
                    )
            );
        }

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
