package pt.isel.ls;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.command.utils.CommandUtils;
import pt.isel.ls.model.Session;
import pt.isel.ls.sql.Sql;
import pt.isel.ls.view.command.GetCinemaIDSessionIDView;
import pt.isel.ls.view.command.GetCinemaIDSessionsView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionsView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;


public class Session_tests {
    Connection con = null;
    int movieID = 0;
    int cinemaID = 0;
    int theaterID = 0;

    public void createSession(Connection con) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO MOVIE VALUES('title1', 2000, 90)");
            stmt.executeUpdate();

            stmt = con.prepareStatement("SELECT mid FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            movieID = 0;
            if(rs.next()) movieID = rs.getInt(1);

            stmt = con.prepareStatement("INSERT INTO CINEMA VALUES('nameTest1', 'cityTest')");
            //stmt = con.prepareStatement("INSERT INTO CINEMA VALUES('nameTest2', 'cityTest')");
            stmt.executeUpdate();

            stmt = con.prepareStatement("SELECT cid FROM CINEMA");
            rs = stmt.executeQuery();
            cinemaID = 0;
            if(rs.next()) cinemaID = rs.getInt(1);
            stmt = con.prepareStatement("INSERT INTO THEATER VALUES(10, 5, 20, 'theaterName', " + cinemaID + ")");
            stmt.executeUpdate();

            stmt = con.prepareStatement("SELECT tid FROM THEATER");
            rs = stmt.executeQuery();
            theaterID = 0;
            if(rs.next()) theaterID = rs.getInt(1);

            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaID + "/theaters/" + theaterID + "/sessions", "date=2018/4/1&mid=" + movieID}, new CommandUtils()));
            //stmt = con.prepareStatement("INSERT INTO CINEMA_SESSION VALUES('2018/4/1', " + movieID + ", " + theaterID + ")");
            //stmt.executeUpdate();


            /*Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas", "name=cinema1&city=cidade1"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters", "name=sala1&rows=12&seats=18"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters", "name=sala2&rows=12&seats=18"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/2/sessions", "date=2018/4/1&mid=1"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/2/sessions", "date="+date+"&mid=2"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/2/sessions", "date=2018/4/1&mid=3"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/1/sessions", "date=2018/4/1&mid=1"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/1/sessions", "date=2018/4/1&mid=2"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/1/sessions", "date="+date+"&mid=3"}, new CommandUtils()));
            */
        } catch (SQLException | CommandNotFoundException | InvalidCommandParametersException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void start_tests(){

    }

    @Test
    public void insert_sessions() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            PreparedStatement stmt = con.prepareStatement("SELECT s.tid, s.mid FROM CINEMA_SESSION AS s");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                assertEquals(theaterID, rs.getInt(1));
                assertEquals(movieID, rs.getInt(2));
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
    public void get_sessions_by_cinema() {
       try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            GetCinemaIDSessionsView view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{
                    "GET", "/cinemas/" + cinemaID + "/sessions"}, new CommandUtils()));
           assertEquals("2018-04-01", view.getSingle().getDate().toString());
           assertEquals("title1", view.getSingle().getMovie().getTitle());

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
    public void get_sessions_by_theater() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);

            GetCinemaIDTheaterIDSessionsView view = (GetCinemaIDTheaterIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{
                    "GET", "/cinemas/" + cinemaID + "/theaters/" + theaterID + "/sessions"}, new CommandUtils()));
            assertEquals("2018-04-01", view.getSingle().getDate().toString());
            assertEquals("title1", view.getSingle().getMovie().getTitle());

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
    public void get_session_by_id() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            PreparedStatement stmt = con.prepareStatement("SELECT sid FROM CINEMA_SESSION");
            int sid;
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sid = rs.getInt(1);
                GetCinemaIDSessionIDView sessionIDView = (GetCinemaIDSessionIDView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{
                        "GET", "/cinemas/" + cinemaID + "/sessions/" + sid}, new CommandUtils()));
                assertEquals("2018-04-01", sessionIDView.getSingle().getDate().toString());
                assertEquals("title1", sessionIDView.getSingle().getMovie().getTitle());
            }

        } catch (SQLException | CommandNotFoundException | InvalidCommandParametersException e) {
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
    public void get_sessions_today_by_cinema(){
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            PreparedStatement stmt = con.prepareStatement("SELECT s.sid,t.cid,s.Date FROM CINEMA_SESSION AS s INNER JOIN THEATER AS t ON t.tid=s.tid WHERE s.Date="+date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id=rs.getInt(1);
                int cid=rs.getInt(2);
                GetCinemaIDSessionIDView sessionIDView = (GetCinemaIDSessionIDView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{
                        "GET", "/cinemas/1/sessions/today"}, new CommandUtils()));
                assertEquals(id, sessionIDView.getSingle().getId());
                assertEquals(cid, sessionIDView.getSingle().getId());
            }

        } catch (SQLException | CommandNotFoundException | InvalidCommandParametersException e) {
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
    public void get_sessions_today_by_theater(){}

}
