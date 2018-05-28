package pt.isel.ls.core.common.commands.db_queries;

import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataEnum.PD_DOSENT_EXIST;
import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataEnum.PD_FAILED;
import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataEnum.PD_OK;
import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataType.PDT_MOVIE;
import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataType.PDT_SESSION;
import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataType.PDT_THEATER;


public class SessionsSQL {

    /**
     * Post Session
     * @param con SQL Connection
     * @param theaterID theaterID
     * @param event date for the session
     * @param movieID movieID
     * @return Returns id of posted session
     * @throws SQLException SQLException
     */
    public static PostData postSession(Connection con, int theaterID, Date event, int movieID) throws SQLException {
        Date date, newDate;
        Timestamp timestamp;
        PreparedStatement stmt;
        ResultSet rs;

        boolean canPost = true;
        int duration, eventDuration=0, seatsAvailable=0;

        Movie movie = MoviesSQL.queryID(con, movieID);
        if (movie == null) {
            return new PostData(PD_DOSENT_EXIST, PDT_MOVIE);
        }
        eventDuration = movie.getDuration();
        Date newEvent = new Date(event.getTime() + eventDuration*60000);


        stmt = con.prepareStatement(
                "SELECT s.Date, m.Duration, t.SeatsAvailable FROM CINEMA_SESSION AS s " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid "+
                "INNER JOIN THEATER AS t ON t.tid=s.tid "+
                "WHERE s.tid=?"
        );
        stmt.setInt(1, theaterID);
        stmt.execute();
        rs = stmt.executeQuery();

        while (rs.next()) { //Check if DATE is already in use
            if (seatsAvailable == 0) seatsAvailable = rs.getInt(3);
            timestamp = rs.getTimestamp(1);
            if (timestamp != null)
                date = new java.util.Date(timestamp.getTime());
            else {
                date = rs.getDate(1);
            }
            duration = rs.getInt(2);
            newDate = new Date(date.getTime() + (duration*60000));
            if ((date.before(event) && newDate.after(event)) || (event.before(date) && newEvent.after(date)) || date.equals(event) || newDate.equals(newEvent) || date.equals(newEvent) || event.equals(newDate)) {
                canPost = false;
                break;
            }
        }
        if(seatsAvailable==0 && canPost ){
            stmt = con.prepareStatement(
                    "SELECT t.SeatsAvailable FROM THEATER AS t "+
                    "WHERE t.tid=?"
            );
            stmt.setInt(1, theaterID);
            stmt.execute();
            rs = stmt.executeQuery();
            if(rs.next())
                seatsAvailable = rs.getInt(1);
            else {
                return new PostData(PD_DOSENT_EXIST, PDT_THEATER);
            }
        }
        if (canPost ) { //If DATE free, then POST
            stmt = con.prepareStatement(
                    "INSERT INTO CINEMA_SESSION VALUES (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            stmt.setTimestamp(1,new Timestamp(event.getTime()));
            stmt.setInt(2, movieID);
            stmt.setInt(3, theaterID);
            stmt.setInt(4, seatsAvailable);
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            int id = 0;
            if (rs.next())
                id = rs.getInt(1);
            return new PostData(PD_OK, PDT_SESSION, id);
        }
        else {
            return new PostData(PD_FAILED);
        }
    }

    /**
     * Get list of all sessions and place them in a linkedlist.
     * @param con SQL Connection
     * @param cinemaID cinemaID
     * @return Returns list of sessions
     * @throws SQLException SQLException
     */
    public static List<Session> queryForCinema(Connection con, int cinemaID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT s.sid, s.SeatsAvailable, s.Date, m.mid, t.tid " +
                "FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE cid=? " +
                "ORDER BY s.Date"
        );
        stmt.setInt(1, cinemaID);
        ResultSet rs = stmt.executeQuery();
        LinkedList<Session> sessions = new LinkedList<>();

        while(rs.next()){
            sessions.add(new Session(rs.getInt(1), rs.getInt(2), rs.getTimestamp(3), rs.getInt(4), cinemaID, rs.getInt(5)));
        }

        return sessions;
    }

    /**
     * Get the session with requested ID
     * @param con SQL Connection
     * @param sessionID sessionID
     * @return Returns Session
     * @throws SQLException SQLException
     */
    public static Session queryID(Connection con, int sessionID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT s.sid, s.SeatsAvailable, s.Date, m.mid, t.cid, t.tid " +
                "FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE s.sid=?"
        );
        stmt.setInt(1, sessionID);
        ResultSet rs = stmt.executeQuery();

        if(!rs.next())
            return null;

        return new Session(rs.getInt(1), rs.getInt(2), rs.getTimestamp(3), rs.getInt(4), rs.getInt(5), rs.getInt(6));
    }

    /**
     * Get the sessions of the requested theater
     * @param con SQL Connection
     * @param theaterID theaterID
     * @return Returns list of sessions
     * @throws SQLException SQLException
     */
    public static List<Session> queryForTheater(Connection con, int theaterID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT s.sid, s.SeatsAvailable, s.Date, m.mid, t.cid, t.tid " +
                "FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE t.tid=? " +
                "ORDER BY s.Date"
        );
        stmt.setInt(1, theaterID);
        ResultSet rs = stmt.executeQuery();
        LinkedList<Session> sessions = new LinkedList<>();

        while(rs.next()) {
            sessions.add(new Session(rs.getInt(1), rs.getInt(2), rs.getTimestamp(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)));
        }

        return sessions;
    }

    /**
     * Get the sessions of the requested theater or cinema dependent of the condition.
     * @param con SQL Connection
     * @param id id
     * @param date date
     * @param condition filter cid / tid / ...
     * @return Returns list of sessions
     * @throws SQLException SQLException
     */
    private static List<Session> queryForDate(Connection con, int id, String date, String condition) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT s.sid, s.SeatsAvailable, s.Date, s.mid, t.cid, t.tid " +
                "FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE "+condition+" AND (CAST(s.Date AS DATE))=? " +
                "ORDER BY s.Date"
        );
        stmt.setInt(1, id);
        stmt.setString(2, date);
        ResultSet rs = stmt.executeQuery();
        LinkedList<Session> sessions = new LinkedList<>();

        while(rs.next()) {
            sessions.add(new Session(rs.getInt(1), rs.getInt(2), rs.getTimestamp(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)));
        }

        return sessions;
    }
    public static List<Session> queryForCinemaAndDate(Connection con, int cinemaID, String date) throws SQLException {
        return queryForDate(con, cinemaID, date, "t.cid=?");
    }
    public static List<Session> queryForTheaterAndDate(Connection con, int theaterID, String date) throws SQLException {
        return queryForDate(con, theaterID, date, "t.tid=?");
    }

    /**
     * Get the number of available seats for this session
     * @param con SQL Connection
     * @param sessionID sessionID
     * @return Returns int value of available seats
     */
    public static int queryAvailableSeats(Connection con, int sessionID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT CINEMA_SESSION.SeatsAvailable " +
                "FROM CINEMA_SESSION " +
                "WHERE CINEMA_SESSION.sid=?");
        stmt.setInt(1, sessionID);
        ResultSet rs = stmt.executeQuery();

        if(!rs.next())
            return NA_AVAILABLESEATS;

        return rs.getInt(1);
    }
    public static final int NA_AVAILABLESEATS = -1;

    /**
     * Get list of all sessions that are playing the movieID
     * @param con SQL Connection
     * @param movieID movieID
     * @return Returns list of sessions
     * @throws SQLException SQLException
     */
    public static List<Session> queryPlayingMovieID(Connection con, int movieID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT s.sid, s.SeatsAvailable, s.Date, m.mid, t.cid, t.tid " +
                "FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid AND m.mid=? " +
                "ORDER BY s.Date"
        );
        stmt.setInt(1, movieID);
        ResultSet rs = stmt.executeQuery();
        LinkedList<Session> sessions = new LinkedList<>();

        while(rs.next()) {
            sessions.add(new Session(rs.getInt(1), rs.getInt(2), rs.getTimestamp(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)));
        }

        return sessions;
    }

    /**
     * Get list of sessions for this date playing this movieID with these conditions
     * @param con SQL Connection
     * @param movieID movieID
     * @param date date
     * @param condition1 filter city / cid / ...
     * @param condition2 filter seats available ...
     * @return Returns list of sessions
     * @throws SQLException SQLException
     */
    private static List<Session> queryPlayingMovieIDForDateCondition(Connection con, int movieID, String date, String condition1, String condition2) throws  SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT s.sid, s.SeatsAvailable, s.Date, m.mid, t.cid, t.tid " +
                "FROM MOVIE AS m " +
                "INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid "+condition1+" "+
                "WHERE m.mid="+movieID+" AND (CAST(s.Date AS DATE))=? "+condition2+" "+
                "ORDER BY s.Date"
        );
        stmt.setString(1, date);
        ResultSet rs = stmt.executeQuery();
        LinkedList<Session> sessions = new LinkedList<>();

        while(rs.next()) {
            sessions.add(new Session(rs.getInt(1), rs.getInt(2), rs.getTimestamp(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)));
        }

        return sessions;
    }
    public static List<Session> queryPlayingMovieIDForDateAndCity(Connection con, int movieID, String date, String city) throws SQLException {
        return queryPlayingMovieIDForDateCondition(con, movieID, date, "AND c.City='"+city+"'", "");
    }
    public static List<Session> queryPlayingMovieIDForDateAndCinemaID(Connection con, int movieID, String date, int cinemaID) throws SQLException {
        return queryPlayingMovieIDForDateCondition(con, movieID, date, "AND c.cid="+cinemaID, "");
    }
    public static List<Session> queryPlayingMovieIDForDateAndAvailableAbove(Connection con, int movieID, String date, int available) throws SQLException {
        return queryPlayingMovieIDForDateCondition(con, movieID, date, "", "AND s.SeatsAvailable>="+available);
    }
    public static List<Session> queryPlayingMovieIDForDate(Connection con, int movieID, String date) throws SQLException {
        return queryPlayingMovieIDForDateCondition(con, movieID, date, "", "");
    }
}
