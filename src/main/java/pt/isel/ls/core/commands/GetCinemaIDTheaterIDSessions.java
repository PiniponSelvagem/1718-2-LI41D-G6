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

public class GetCinemaIDTheaterIDSessions extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "INNER JOIN SEATS ON SEATS.sid=s.sid " +
                "WHERE cid=? AND t.tid=?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        stmt.setString(2, cmdBuilder.getId(String.valueOf(THEATER_ID)));
        ResultSet rs = stmt.executeQuery();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        int id, mid, tid, availableSeats, rows, seatsRow, cid, year, duration;
        Timestamp dateSession;
        String theaterName, title;


        while(rs.next()){
            id = rs.getInt(1);
            dateSession = rs.getTimestamp(2);
            mid = rs.getInt(3);
            tid = rs.getInt(4);
            availableSeats = rs.getInt(15);
            rows = rs.getInt(7);
            seatsRow = rs.getInt(8);
            theaterName = rs.getString(9);
            cid = rs.getInt(10);
            title = rs.getString(12);
            year = rs.getInt(13);
            duration = rs.getInt(14);

            data.add(
                    new Session(id, dateSession,
                            new Movie(mid, title, year, duration),
                            new Theater(tid, theaterName, rows, seatsRow, availableSeats, cid),
                            cid
                    )
            );
        }

        return new GetCinemaIDTheaterIDSessionsView(
                data,
                Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))),
                Integer.parseInt(cmdBuilder.getId(String.valueOf(THEATER_ID)))
        );
    }
}
