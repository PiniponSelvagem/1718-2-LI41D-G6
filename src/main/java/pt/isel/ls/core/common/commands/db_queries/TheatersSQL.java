package pt.isel.ls.core.common.commands.db_queries;

import pt.isel.ls.model.Theater;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static pt.isel.ls.core.common.commands.db_queries.SQLData.PostDataEnum.PD_OK;
import static pt.isel.ls.core.common.commands.db_queries.SQLData.PostDataType.PDT_THEATER;

public class TheatersSQL {

    /**
     * Post Theater
     * @param con SQL Connection
     * @param cinemaID Cinema ID
     * @param name Theater name
     * @param rows Rows
     * @param seatsRow Seats per row
     * @return Returns id of the theater posted
     * @throws SQLException SQLException
     */
    public static SQLData postTheater(Connection con, String cinemaID, String name, int rows, int seatsRow) throws SQLException {
        int seats = rows*seatsRow;
        PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO THEATER (SeatsAvailable, Rows, Seats, Theater_Name, cid) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        stmt.setInt(1, seats);
        stmt.setInt(2, rows);
        stmt.setInt(3, seatsRow);
        stmt.setString(4, name);
        stmt.setInt(5, Integer.parseInt(cinemaID));
        stmt.execute();

        ResultSet rs = stmt.getGeneratedKeys();
        int id = 0;
        if(rs.next())
            id = rs.getInt(1);

        return new SQLData(PD_OK, PDT_THEATER, id);
    }

    /**
     * Get list of all theaters and place them in a linkedlist.
     * @param con SQL Connection
     * @param cinemaID cinemaID
     * @return Returns list of theaters
     * @throws SQLException SQLException
     */
    public static List<Theater> queryAll(Connection con, String cinemaID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT t.tid, t.Theater_Name, t.SeatsAvailable, t.Rows, t.Seats, t.cid " +
                "FROM THEATER AS t " +
                "WHERE t.cid=? " +
                "ORDER BY t.tid"
        );
        stmt.setInt(1, Integer.parseInt(cinemaID));
        ResultSet rs = stmt.executeQuery();
        LinkedList<Theater> theaters = new LinkedList<>();

        while(rs.next()){
            theaters.add(new Theater(rs.getString(1), rs.getString(2), rs.getInt(4), rs.getInt(5), rs.getInt(3), rs.getString(6)));
        }

        return theaters;
    }

    /**
     * Get the theater with requested ID
     * @param con SQL Connection
     * @param theaterID theaterID
     * @return Returns Theater
     * @throws SQLException SQLException
     */
    public static Theater queryID(Connection con, String theaterID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT t.tid, t.Theater_Name, t.SeatsAvailable, t.Rows, t.Seats, t.cid " +
                "FROM THEATER AS t " +
                "WHERE t.tid=?"
        );
        stmt.setInt(1, Integer.parseInt(theaterID));
        ResultSet rs = stmt.executeQuery();

        if(!rs.next())
            return null;

        return new Theater(rs.getString(1), rs.getString(2), rs.getInt(4), rs.getInt(5), rs.getInt(3), rs.getString(6));
    }

    /**
     * Get list of all theaters for the cinemaID and place them in a linkedlist.
     * Key value is tid
     * @param con SQL Connection
     * @param cinemaID cinemaID
     * @return Returns list of theaters
     * @throws SQLException SQLException
     */
    public static Map<Integer, Theater> queryForCinema(Connection con, String cinemaID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT t.tid, t.Theater_Name, t.SeatsAvailable, t.Rows, t.Seats, c.cid " +
                "FROM THEATER AS t " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid AND c.cid=?"
        );
        stmt.setInt(1, Integer.parseInt(cinemaID));
        ResultSet rs = stmt.executeQuery();
        HashMap<Integer, Theater> theaters = new HashMap<>();

        while(rs.next()) {
            theaters.put(rs.getInt(1), new Theater(rs.getString(1), rs.getString(2), rs.getInt(4), rs.getInt(5), rs.getInt(3), rs.getString(6)));
        }

        return theaters;
    }

    /**
     * Get the theater for this sessionID
     * @param con SQL Connection
     * @param sessionID sessionID
     * @return Returns movie
     * @throws SQLException SQLException
     */
    public static Theater queryForSession(Connection con, String sessionID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT t.tid, t.Theater_Name, t.SeatsAvailable, t.Rows, t.Seats, t.cid " +
                "FROM THEATER AS t " +
                "INNER JOIN CINEMA_SESSION AS s ON t.tid=s.tid " +
                "WHERE s.sid=?"
        );
        stmt.setInt(1, Integer.parseInt(sessionID));
        ResultSet rs = stmt.executeQuery();

        if(!rs.next())
            return null;

        return new Theater(rs.getString(1), rs.getString(2), rs.getInt(4), rs.getInt(5), rs.getInt(3), rs.getString(6));
    }

    /**
     * Get all theaters for this date playing this movieID with these conditions
     * @param con SQL Connection
     * @param movieID movieID
     * @param date date
     * @param condition1 filter city / cid / ...
     * @param condition2 filter seats available ...
     * @return Returns list of theaters
     * @throws SQLException SQLException
     */
    private static Map<Integer, Theater> queryPlayingMovieIDForDateCondition(Connection con, String movieID, String date, String condition1, String condition2) throws  SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT t.tid, t.Theater_Name, t.SeatsAvailable, t.Rows, t.Seats, t.cid " +
                "FROM MOVIE AS m " +
                "INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid "+condition1+" "+
                "WHERE m.mid=? AND (CAST(s.Date AS DATE))=? "+condition2+" "+
                "ORDER BY s.Date"
        );
        stmt.setInt(1, Integer.parseInt(movieID));
        stmt.setString(2, date);
        ResultSet rs = stmt.executeQuery();
        HashMap<Integer, Theater> theaters = new HashMap<>();

        while(rs.next()) {
            theaters.put(rs.getInt(1), new Theater(rs.getString(1), rs.getString(2), rs.getInt(4), rs.getInt(5), rs.getInt(3), rs.getString(6)));
        }

        return theaters;
    }
    public static Map<Integer, Theater> queryPlayingMovieIDForDateAndCity(Connection con, String movieID, String date, String city) throws SQLException {
        return queryPlayingMovieIDForDateCondition(con, movieID, date, "AND c.City='"+city+"'", "");
    }
    public static Map<Integer, Theater> queryPlayingMovieIDForDateAndCinemaID(Connection con, String movieID, String date, int cinemaID) throws SQLException {
        return queryPlayingMovieIDForDateCondition(con, movieID, date, "AND c.cid="+cinemaID, "");
    }
    public static Map<Integer, Theater> queryPlayingMovieIDForDateAndAvailableAbove(Connection con, String movieID, String date, int available) throws SQLException {
        return queryPlayingMovieIDForDateCondition(con, movieID, date, "", "AND s.SeatsAvailable>="+available);
    }
    public static Map<Integer, Theater> queryPlayingMovieIDForDate(Connection con, String movieID, String date) throws SQLException {
        return queryPlayingMovieIDForDateCondition(con, movieID, date, "", "");
    }
}
