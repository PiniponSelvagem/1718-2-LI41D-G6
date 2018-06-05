package pt.isel.ls.core.common.commands.db_queries;

import pt.isel.ls.model.Cinema;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static pt.isel.ls.core.common.commands.db_queries.SQLData.PostDataEnum.PD_OK;
import static pt.isel.ls.core.common.commands.db_queries.SQLData.PostDataType.PDT_CINEMA;

public class CinemasSQL {

    /**
     * Post cinema
     * @param con SQL Connection
     * @param name cinema name
     * @param city city
     * @return Returns the id of the cinema posted
     * @throws SQLException SQLException
     */
    public static SQLData postCinema(Connection con, String name, String city) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO CINEMA (Name, City) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        stmt.setString(1, name);
        stmt.setString(2, city);
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        int id = 0;
        if(rs.next())
            id = rs.getInt(1);

        return new SQLData(PD_OK, PDT_CINEMA, id);
    }

    /**
     * Get list of all cinemas and place them in a linkedlist.
     * @param con SQL Connection
     * @return Returns list of cinemas
     * @throws SQLException SQLException
     */
    public static List<Cinema> queryAll(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(
                "SELECT c.cid, c.Name, c.City " +
                "FROM CINEMA AS c " +
                "ORDER BY c.cid"
        );
        ResultSet rs = stmt.executeQuery();
        LinkedList<Cinema> cinemas = new LinkedList<>();

        while(rs.next()){
            cinemas.add(new Cinema(rs.getString(1), rs.getString(2), rs.getString(3)));
        }

        return cinemas;
    }

    /**
     * Get the cinema with requested ID
     * @param con SQL Connection
     * @param cinemaID cinemaID
     * @return Returns Cinema
     * @throws SQLException SQLException
     */
    public static Cinema queryID(Connection con, String cinemaID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT c.cid, c.Name, c.City " +
                "FROM CINEMA AS c " +
                "WHERE cid=?"
        );
        stmt.setInt(1, Integer.parseInt(cinemaID));
        ResultSet rs = stmt.executeQuery();

        if(!rs.next())
            return null;

        return new Cinema(rs.getString(1), rs.getString(2), rs.getString(3));
    }

    /**
     * Get list of all cinemas that are playing the movieID
     * @param con SQL Connection
     * @param movieID movieID
     * @return Returns list of cinemas
     * @throws SQLException SQLException
     */
    public static List<Cinema> queryPlayingMovieID(Connection con, String movieID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT DISTINCT c.cid, c.Name, c.City " +
                "FROM CINEMA AS c " +
                "INNER JOIN THEATER as t on c.cid=t.cid " +
                "INNER JOIN CINEMA_SESSION AS cs ON cs.tid=t.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=cs.mid AND m.mid=?"
        );
        stmt.setInt(1, Integer.parseInt(movieID));
        ResultSet rs = stmt.executeQuery();
        LinkedList<Cinema> cinemas = new LinkedList<>();

        while(rs.next()) {
            cinemas.add(new Cinema(rs.getString(1), rs.getString(2), rs.getString(3)));
        }

        return cinemas;
    }
}
