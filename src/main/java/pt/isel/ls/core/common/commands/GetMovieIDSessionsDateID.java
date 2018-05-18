package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetMovieIDSessionsDateIDView;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SESSIONS;

public class GetMovieIDSessionsDateID extends Command {

    private Timestamp date = null;
    private DataContainer data;
    private static final String COLUMNS = "s.sid, s.Date, m.mid, t.tid, s.SeatsAvailable, t.Rows, t.Seats, t.Theater_Name, c.cid, m.Title, m.Release_Year, m.Duration, t.SeatsAvailable ";
    private static final String FROM = "FROM MOVIE AS m INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid ";
    private static final String INNER_JOIN_THEATER = "INNER JOIN THEATER AS t ON t.tid=s.tid ";

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+MOVIE_ID_FULL
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+DATE+DIR_SEPARATOR+DATE_ID_FULL;
}

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException, CommandException {

        LocalDate localDate;
        String str = cmdBuilder.getId(DATE_ID.toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        localDate = LocalDate.parse(str, formatter);
        Date date1 = Date.valueOf(localDate);

        data = new DataContainer(cmdBuilder.getHeader());

        if (cmdBuilder.hasParameter(CITY.toString())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT " + COLUMNS +
                            FROM +
                            INNER_JOIN_THEATER +
                            "INNER JOIN CINEMA AS c ON t.cid=c.cid AND c.City=? " +
                            "WHERE m.mid=? AND (CAST(s.Date AS DATE))=?"
            );
            stmt.setString(1, cmdBuilder.getParameter(CITY.toString()));
            stmt.setString(2, cmdBuilder.getId(MOVIE_ID.toString()));
            stmt.setDate(3, date1);
            fillData(stmt.executeQuery());
        }
        else if (cmdBuilder.hasParameter(CINEMA_ID.toString())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT " +COLUMNS +
                            FROM +
                            INNER_JOIN_THEATER +
                            "INNER JOIN CINEMA AS c ON t.cid=c.cid AND c.cid=? " +
                            "WHERE (CAST(s.Date AS DATE))=? AND m.mid=?"
            );
            stmt.setString(1, cmdBuilder.getParameter(CINEMA_ID.toString()));
            stmt.setDate(2, date1);
            stmt.setString(3, cmdBuilder.getId(MOVIE_ID.toString()));
            fillData(stmt.executeQuery());
        }
        else if (cmdBuilder.hasParameter(AVAILABLE.toString())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT " + COLUMNS +
                            FROM +
                            INNER_JOIN_THEATER +
                            "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                            "WHERE (CAST(s.Date AS DATE))=? AND m.mid=? AND s.SeatsAvailable>=?"
            );
            stmt.setDate(1, date1);
            stmt.setString(2, cmdBuilder.getId(MOVIE_ID.toString()));
            stmt.setString(3, cmdBuilder.getParameter(AVAILABLE.toString()));
            fillData(stmt.executeQuery());
        }
        else {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT " + COLUMNS +
                            FROM +
                            INNER_JOIN_THEATER +
                            "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                            "WHERE (CAST(s.Date AS DATE))=? AND m.mid=?"
            );
            stmt.setDate(1, date1);
            stmt.setString(2, cmdBuilder.getId(MOVIE_ID.toString()));
            fillData(stmt.executeQuery());
        }
        return new GetMovieIDSessionsDateIDView(
                data,
                Integer.parseInt(cmdBuilder.getId(MOVIE_ID.toString())),
                date
        );
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }


    /**
     * * Fills data with info from rs.
     * @param rs ResultSet
     */
    private void fillData(ResultSet rs) throws SQLException {
        int sid, mid, tid, availableSeats, rows, seatsRow, cid, year, duration, seats;
        String theaterName, title;

        LinkedList<Session> sessions = new LinkedList<>();
        while(rs.next()) {
            sid = rs.getInt(1);
            date = rs.getTimestamp(2);
            mid = rs.getInt(3);
            tid = rs.getInt(4);
            availableSeats = rs.getInt(5);
            rows = rs.getInt(6);
            seatsRow = rs.getInt(7);
            theaterName = rs.getString(8);
            cid = rs.getInt(9);
            title = rs.getString(10);
            year = rs.getInt(11);
            duration = rs.getInt(12);
            seats = rs.getInt(13);

            sessions.add(
                    new Session(sid, availableSeats, date,
                            new Movie(mid, title, year, duration),
                            new Theater(tid, theaterName, rows, seatsRow, seats, cid),
                            cid
                    )
            );
        }
        data.add(D_SESSIONS, sessions);
    }
}
