package pt.isel.ls.core.common.commands.db_queries;

import pt.isel.ls.model.Movie;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static pt.isel.ls.core.common.commands.db_queries.SQLData.PostDataEnum.PD_OK;
import static pt.isel.ls.core.common.commands.db_queries.SQLData.PostDataType.PDT_MOVIE;

public class MoviesSQL {

    /**
     * Post movie
     * @param con SQL Connection
     * @param title Title
     * @param releaseYear Release year
     * @param duration Duration in minutes
     * @return Returns the id of the movie posted
     * @throws SQLException SQLException
     */
    public static SQLData postMovie(Connection con, String title, int releaseYear, int duration) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO MOVIE (Title, ReleaseYear, Duration) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        stmt.setString(1, title);
        stmt.setInt(2, releaseYear);
        stmt.setInt(3, duration);
        stmt.executeUpdate();

        int id = 0;
        ResultSet rs = stmt.getGeneratedKeys();
        if(rs.next())
            id = rs.getInt(1);

        return new SQLData(PD_OK, PDT_MOVIE, id);
    }

    /**
     * Get list of all movies and place them in a linkedlist.
     * @param con SQL Connection
     * @return Returns list of movies
     * @throws SQLException SQLException
     */
    public static List<Movie> queryAll(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT m.mid, m.Title, m.Release_Year, m.Duration " +
                "FROM MOVIE AS m " +
                "ORDER BY m.Title"
        );
        ResultSet rs = stmt.executeQuery();
        LinkedList<Movie> movies = new LinkedList<>();

        while(rs.next()){
            movies.add(new Movie(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
        }

        return movies;
    }

    /**
     * Get the movie with requested ID
     * @param con SQL Connection
     * @param movieID movieID
     * @return Returns Movie
     * @throws SQLException SQLException
     */
    public static Movie queryID(Connection con, String movieID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT m.mid, m.Title, m.Release_Year, m.Duration " +
                "FROM MOVIE AS m " +
                "WHERE mid=?"
        );
        stmt.setInt(1, Integer.parseInt(movieID));
        ResultSet rs = stmt.executeQuery();

        if(!rs.next())
            return null;

        return new Movie(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
    }

    /**
     * Get all movies for this cinema and place them in a HashMap.
     * Key value is mid
     * @param con SQL Connection
     * @param cinemaID cinemaID
     * @return Returns hashmap of movies
     * @throws SQLException SQLException
     */
    public static Map<Integer, Movie> queryForCinema(Connection con, String cinemaID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT DISTINCT m.mid, m.Title, m.Release_Year, m.Duration " +
                "FROM MOVIE AS m " +
                "INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN CINEMA AS c ON c.cid=t.cid " +
                "WHERE c.cid=? " +
                "ORDER BY m.Title"
        );
        stmt.setInt(1, Integer.parseInt(cinemaID));
        ResultSet rs = stmt.executeQuery();
        HashMap<Integer, Movie> movies = new HashMap<>();

        while(rs.next()){
            movies.put(rs.getInt(1), new Movie(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
        }

        return movies;
    }

    /**
     * Get the movie for this sessionID
     * @param con SQL Connection
     * @param sessionID sessionID
     * @return Returns movie
     * @throws SQLException SQLException
     */
    public static Movie queryForSession(Connection con, String sessionID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT m.mid, m.Title, m.Release_Year, m.Duration " +
                "FROM MOVIE AS m " +
                "INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                "WHERE s.sid=? " +
                "ORDER BY m.Title"
        );
        stmt.setInt(1, Integer.parseInt(sessionID));
        ResultSet rs = stmt.executeQuery();

        if(!rs.next())
            return null;

        return new Movie(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
    }

    /**
     * Get all movies for the sessions of this cinema, and place them in a HashMap.
     * Key value is sid
     * @param con SQL Connection
     * @param cinemaID cinemaID
     * @return Returns hashmap of movies
     * @throws SQLException SQLException
     */
    public static Map<Integer, Movie> queryForSessionAndTheater(Connection con, String cinemaID, String theaterID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT m.mid, m.Title, m.Release_Year, m.Duration " +
                "FROM MOVIE AS m " +
                "INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN CINEMA AS c ON c.cid=t.cid " +
                "WHERE c.cid=? AND t.tid=? " +
                "ORDER BY m.Title"
        );
        stmt.setInt(1, Integer.parseInt(cinemaID));
        stmt.setInt(2, Integer.parseInt(theaterID));
        ResultSet rs = stmt.executeQuery();
        HashMap<Integer, Movie> movies = new HashMap<>();

        while(rs.next()){
            movies.put(rs.getInt(1), new Movie(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
        }

        return movies;
    }

    /**
     * Get the movies that play in the requested theater
     * @param con SQL Connection
     * @param theaterID theaterID
     * @return Returns list of sessions
     * @throws SQLException SQLException
     */
    public static Map<Integer, Movie> queryForTheaterAndDate(Connection con, String theaterID, String date) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT m.mid, m.Title, m.Release_Year, m.Duration " +
                "FROM MOVIE AS m " +
                "INNER JOIN CINEMA_SESSION AS s ON s.mid=m.mid " +
                "INNER JOIN THEATER AS t ON t.tid=s.sid " +
                "WHERE t.tid=? AND (CAST(s.Date AS DATE))=?"
        );
        stmt.setInt(1, Integer.parseInt(theaterID));
        stmt.setString(2, date);
        ResultSet rs = stmt.executeQuery();
        HashMap<Integer, Movie> movies = new HashMap<>();

        while(rs.next()){
            movies.put(rs.getInt(1), new Movie(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
        }

        return movies;
    }
}
