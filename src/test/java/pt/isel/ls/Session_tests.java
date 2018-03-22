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
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;


public class Session_tests {

    private Connection con = null;
    int cinemaId;
    int []theatersId= new int[2];
    int []movId= new int[3];
    int []sessionsId= new int[6];

    @BeforeClass
    public static void start_tests(){

    }

    public void createSession(Connection con) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            for (int i = 0; i < 3; i++) {
                String title = "TestTitle";
                title += (i + 1);
                Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/movies", "title=" + title + "&releaseYear=2000&duration=90"}, new CommandUtils()));
            }
            PreparedStatement stmt = con.prepareStatement("SELECT mid FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            int i=0;
            while(rs.next() && i<movId.length) {
                movId[i] = rs.getInt(1);
                i++;
            }
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas", "name=cinema1&city=cidade1"}, new CommandUtils()));
            stmt = con.prepareStatement("SELECT cid FROM CINEMA");
            rs = stmt.executeQuery();
            if(rs.next()) cinemaId = rs.getInt(1);
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters", "name=sala1&rows=12&seats=18"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters", "name=sala2&rows=12&seats=18"}, new CommandUtils()));
            stmt = con.prepareStatement("SELECT tid FROM THEATER");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<theatersId.length) {
                theatersId[i] = rs.getInt(1);
                i++;
            }
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1&mid="+movId[0]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date="+date+"&mid="+movId[1]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1&mid="+movId[2]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1&mid="+movId[0]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1&mid="+movId[1]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date="+date+"&mid="+movId[2]}, new CommandUtils()));
            stmt = con.prepareStatement("SELECT sid FROM CINEMA_SESSION");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<sessionsId.length) {
                sessionsId[i] = rs.getInt(1);
                i++;
            }
        } catch (CommandNotFoundException | InvalidCommandParametersException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insert_sessions() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            PreparedStatement stmt = con.prepareStatement("SELECT s.sid, s.tid, s.mid FROM CINEMA_SESSION AS s");
            ResultSet rs = stmt.executeQuery();
            int i=0, j=0, t=0;
            while (rs.next()) {
                if(i>2)t=1;
                if(j==3)j=0;
                assertEquals(rs.getInt(1),sessionsId[i]);
                assertEquals(rs.getInt(2),theatersId[t]);
                assertEquals(rs.getInt(3),movId[j]);
                i++;
                j++;
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
            GetCinemaIDSessionsView view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/sessions"}, new CommandUtils()));
            LinkedList<Session> sessions;
            sessions = view.getList();
            int i = 0;
            for (Session s : sessions) {
                assertEquals(sessionsId[i], s.getId());
                assertEquals(cinemaId, s.getCinemaID());
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
            for(int t=0; t<theatersId.length; t++) {
                GetCinemaIDTheaterIDSessionsView view = (GetCinemaIDTheaterIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/theaters/" + theatersId[t] + "/sessions"}, new CommandUtils()));
                LinkedList<Session> sessions;
                sessions = view.getList();
                for (Session s : sessions) assertEquals(theatersId[t], s.getTheater().getId());
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
                GetCinemaIDSessionIDView sessionIDView = (GetCinemaIDSessionIDView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/sessions/" + id}, new CommandUtils()));
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
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            GetCinemaIDSessionsView view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/sessions/today"}, new CommandUtils()));
            LinkedList<Session> sessions = view.getList();
            int i=1;
            for (Session s : sessions){
                assertEquals(sessionsId[i], s.getId());
                assertEquals(cinemaId, s.getCinemaID());
                assertEquals(date, s.getDate().toString());
                i+=4;
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
    public void get_sessions_today_by_theater(){
		//line 255 command never identified
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            for(int t=0;t<theatersId.length;t++) {
                GetCinemaIDTheaterIDSessionsView view = (GetCinemaIDTheaterIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/theaters/" + theatersId[t] + "/sessions/today"}, new CommandUtils()));
                LinkedList<Session> sessions = view.getList();
                int i=1;
                for (Session s : sessions){
                    if(t==1) i=5;
                    assertEquals(sessionsId[i], s.getId());
                    assertEquals(theatersId[t], s.getTheater().getId());
                    assertEquals(date, s.getDate().toString());
                }
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
}