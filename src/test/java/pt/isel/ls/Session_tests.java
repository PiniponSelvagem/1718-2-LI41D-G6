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

    public void createSession(Connection con) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            for (int i = 0; i < 3; i++) {
                String title = "TestTitle";
                title += (i + 1);
                Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/movies", "title=" + title + "&year=2000&duration=90"}, new CommandUtils()));
            }
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas", "name=cinema1&city=cidade1"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters", "name=sala1&rows=12&seatsrow=18"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters", "name=sala2&rows=12&seatsrow=18"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/2/sessions", "date=2018/4/1&mid=1"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/2/sessions", "date="+date+"&mid=2"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/2/sessions", "date=2018/4/1&mid=3"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/1/sessions", "date=2018/4/1&mid=1"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/1/sessions", "date=2018/4/1&mid=2"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/1/theaters/1/sessions", "date="+date+"&mid=3"}, new CommandUtils()));
        } catch (CommandNotFoundException | InvalidCommandParametersException e) {
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
            PreparedStatement stmt = con.prepareStatement("SELECT s.sid, s.tid, s.mid FROM CINEMA_SESSION AS s");
            ResultSet rs = stmt.executeQuery();
            int i=0, j=0, t=2;
            while (rs.next()) {
                i++;
                j++;
                if(i>3)t=1;
                assertEquals(rs.getInt(1),i);
                assertEquals(rs.getInt(2),t);
                assertEquals(rs.getInt(3),j);
                if(j==3)j=0;
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
            GetCinemaIDSessionsView view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/1/sessions"}, new CommandUtils()));
            LinkedList<Session> sessions;
            sessions = view.getList();
            int i = 1;
            for (Session s : sessions) {
                assertEquals(i, s.getId());
                assertEquals(1, s.getCinemaID());
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
    public void get_sessions_by_theater() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            for(int t=1; t<3; t++) {
                GetCinemaIDTheaterIDSessionsView view = (GetCinemaIDTheaterIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/1/theaters/" + t + "/sessions"}, new CommandUtils()));
                LinkedList<Session> sessions;
                sessions = view.getList();
                for (Session s : sessions) assertEquals(t, s.getTheater().getId());
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
    public void get_session_by_id() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA_SESSION");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id=rs.getInt(1);
                GetCinemaIDSessionIDView sessionIDView = (GetCinemaIDSessionIDView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/1/sessions/" + id}, new CommandUtils()));
                assertEquals(id, sessionIDView.getSingle().getId());
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
                GetCinemaIDSessionIDView sessionIDView = (GetCinemaIDSessionIDView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/1/sessions/today"}, new CommandUtils()));
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
