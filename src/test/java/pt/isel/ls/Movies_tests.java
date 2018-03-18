package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.command.utils.CommandUtils;
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
                title += (i + 1);
                Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/movies", "title=" + title + "&year=2000&duration=90"}, new CommandUtils()));
            }
        } catch (CommandNotFoundException | InvalidCommandParametersException e) {
            e.printStackTrace();
        }
    }

        @Test
    public void insert_movies() {
        LinkedList<Movie> movies = new LinkedList<>();
        try {
            con = Sql.CreateConnetion();
            con.setAutoCommit(false);

            createMovie(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movies.add(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }
            int i = 1;
            for (Movie m : movies) {
                //assertEquals(i, m.getId());
                assertEquals("TestTitle" + i, m.getTitle());
                i++;
            }

        } catch (SQLException e) {
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
            con = Sql.CreateConnetion();
            con.setAutoCommit(false);

            createMovie(con);
            GetMoviesView view = (GetMoviesView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/movies"}, new CommandUtils()));
            LinkedList<Movie> movies;
            movies = view.getList();
            int i = 1;
            for (Movie m : movies) {
                assertEquals("TestTitle" + i, m.getTitle());
                i++;
            }
        } catch (SQLException | InvalidCommandParametersException | CommandNotFoundException e) {
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
            con = Sql.CreateConnetion();
            con.setAutoCommit(false);
            LinkedList<Movie> movies = new LinkedList<>();

            createMovie(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                movies.add(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }

            int id = movies.getFirst().getId();
            GetMovieIDView movieIDView = (GetMovieIDView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/movies/" + id}, new CommandUtils()));
            assertEquals(id, movieIDView.getMovie().getId());

        } catch (SQLException | CommandNotFoundException | InvalidCommandParametersException e) {
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
