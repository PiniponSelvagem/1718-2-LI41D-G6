package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.sql.Sql;
import pt.isel.ls.view.command.GetMovieIDView;
import pt.isel.ls.view.command.GetMoviesView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;


public class Movies_tests {
    Connection con = null;

    public void createMovie(Connection con) {
        try {
            for (int i = 0; i < 3; i++) {
                String title = "TestTitle";
                title += (i);
                PreparedStatement stmt = con.prepareStatement("INSERT INTO MOVIE VALUES('" + title + "', 2000, 90) ");
                stmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insert_movies() {
        LinkedList<Movie> movies = new LinkedList<>();
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            for (int i = 0; i < 3; i++) {
                String title = "TestTitle";
                title += (i + 1);
                Main.executeView(new CommandBuilder(new String[]{"POST", "/movies", "title=" + title + "&releaseYear=2000&duration=90"}, new CommandUtils()), con);
            }
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movies.add(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }
            int i = 1;
            for (Movie m : movies) {
                assertEquals("TestTitle" + i, m.getTitle());
                i++;
            }

        } catch (SQLException | CommandException e) {
            e.printStackTrace();
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
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createMovie(con);
            GetMoviesView view = (GetMoviesView) Main.executeView(new CommandBuilder(new String[]{"GET", "/movies"}, new CommandUtils()), con);
            DataContainer data;
            data = view.getData();
            Movie movie;
            for(int i = 0; i < data.size(); ){
                movie =  (Movie) data.getData(i);
                assertEquals("TestTitle" + i, movie.getTitle());
                i++;
            }
        } catch (SQLException | CommandException e) {
            e.printStackTrace();
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
            con = Sql.getConnection();
            con.setAutoCommit(false);
            LinkedList<Movie> movies = new LinkedList<>();

            createMovie(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                movies.add(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }

            int id = movies.getFirst().getId();
            GetMovieIDView movieIDView = (GetMovieIDView) Main.executeView(new CommandBuilder(new String[]{"GET", "/movies/" + id}, new CommandUtils()), con);
            DataContainer data;
            data = movieIDView.getData();
            Movie movie = (Movie) data.getData(0);
            assertEquals(id, movie.getId());

        } catch (SQLException | CommandException e) {
            e.printStackTrace();
        }finally {
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
