package pt.isel.ls;

import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.core.exceptions.CommonException;
import pt.isel.ls.core.utils.CommandRequest;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static pt.isel.ls.SqlTest.CreateConnetion;
import static pt.isel.ls.core.common.headers.HeadersAvailable.TEXT_HTML;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


@SuppressWarnings("Duplicates")
public class Movies_tests {
    private final static CommandUtils cmdUtils = new CommandUtils(TEXT_HTML.toString());
    private static final PGSimpleDataSource ds = CreateConnetion();
    Connection con = null;

    public void createMovie(Connection con) {
        try {
            for (int i = 0; i < 3; i++) {
                String title = "TestTitle";
                title += (i);
                PreparedStatement stmt = con.prepareStatement("INSERT INTO MOVIE (Title, Release_Year, Duration) VALUES('" + title + "', 2000, 90) ");
                stmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void insert_movies() {
        LinkedList<Movie> movies = new LinkedList<>();
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            for (int i = 0; i < 3; i++) {
                String title = "TestTitle" + (i+1);
                new CommandRequest(new String[]{"POST", "/movies", "title=" + title + "&releaseYear=2000&duration=90"}, cmdUtils, ds).executeCommand(con);
            }
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movies.add(new Movie(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }
            int i = 1;
            for (Movie m : movies) {
                assertEquals("TestTitle" + i, m.getTitle());
                i++;
            }

        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        } finally {
            if (con != null) {
                try {
                    con.rollback();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void get_movies() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            createMovie(con);
            DataContainer data = new CommandRequest(new String[]{"GET", "/movies"}, cmdUtils, ds).executeCommand(con);
            LinkedList<Movie> movies = (LinkedList<Movie>) data.getData(D_MOVIES);
            Movie movie;
            for(int i = 0; i < movies.size(); ){
                movie = movies.get(i);
                assertEquals("TestTitle" + i, movie.getTitle());
                i++;
            }
        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        } finally {
            if (con != null) {
                try {
                    con.rollback();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Test
    public void get_movie_by_id(){
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            LinkedList<Movie> movies = new LinkedList<>();

            createMovie(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                movies.add(new Movie(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }

            String id = movies.getFirst().getId();
            DataContainer data = new CommandRequest(new String[]{"GET", "/movies/" + id}, cmdUtils, ds).executeCommand(con);
            Movie movie = (Movie) data.getData(D_MOVIE);
            assertEquals(id, movie.getId());

        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        } finally {
            if (con != null){
                try {
                    con.rollback();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
