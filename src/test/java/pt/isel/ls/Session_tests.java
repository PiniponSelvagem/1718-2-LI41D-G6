package pt.isel.ls;

import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.core.exceptions.CommonException;
import pt.isel.ls.core.utils.CommandRequest;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static pt.isel.ls.SqlTest.CreateConnetion;
import static pt.isel.ls.core.common.headers.HeadersAvailable.TEXT_HTML;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


@SuppressWarnings("Duplicates")
public class Session_tests {
    private static final PGSimpleDataSource ds = CreateConnetion();
    private final static CommandUtils cmdUtils = new CommandUtils(TEXT_HTML.toString());
    private Connection con = null;
    String cinemaId;
    String []theatersId= new String[2];
    String []movId= new String[3];
    String []sessionsId= new String[6];

    @BeforeClass
    public static void start_tests(){

    }

    public void createSession(Connection con) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            for (int i = 0; i < 3; i++) {
                String title = "TestTitle" + (i+1);
                new CommandRequest(new String[]{"POST", "/movies", "title=" + title + "&releaseYear=2000&duration=90"}, cmdUtils, ds).executeCommand(con);
            }
            PreparedStatement stmt = con.prepareStatement("SELECT mid FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            int i=0;
            while(rs.next() && i<movId.length) {
                movId[i] = rs.getString(1);
                i++;
            }
            new CommandRequest(new String[]{"POST", "/cinemas", "name=cinema1&city=cidade1"}, cmdUtils, ds).executeCommand(con);
            stmt = con.prepareStatement("SELECT cid FROM CINEMA");
            rs = stmt.executeQuery();
            if(rs.next()) cinemaId = rs.getString(1);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters", "name=sala1&row=12&seat=18"}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters", "name=sala2&row=12&seat=18"}, cmdUtils, ds).executeCommand(con);
            stmt = con.prepareStatement("SELECT tid FROM THEATER");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<theatersId.length) {
                theatersId[i] = rs.getString(1);
                i++;
            }
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1+10:00&mid="+movId[0]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1+13:00&mid="+movId[1]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1+16:30&mid="+movId[2]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1+20:00&mid="+movId[0]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date="+date+"+15:30&mid="+movId[1]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date="+date+"+19:00&mid="+movId[2]}, cmdUtils, ds).executeCommand(con);
            stmt = con.prepareStatement("SELECT sid FROM CINEMA_SESSION");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<sessionsId.length) {
                sessionsId[i] = rs.getString(1);
                i++;
            }
        } catch (SQLException | CommonException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void insert_sessions() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            PreparedStatement stmt = con.prepareStatement("SELECT s.sid, s.tid, s.mid FROM CINEMA_SESSION AS s");
            ResultSet rs = stmt.executeQuery();
            int i=0, j=0, t=0;
            while (rs.next()) {
                if(i>2)t=1;
                if(j==3)j=0;
                assertEquals(rs.getString(1),sessionsId[i]);
                assertEquals(rs.getString(2),theatersId[t]);
                assertEquals(rs.getString(3),movId[j]);
                i++;
                j++;
            }
        } catch (SQLException e) {
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
    public void session_not_posted(){
        try{
            con = ds.getConnection();
            con.setAutoCommit(false);
            createSession(con);

            PreparedStatement stmt = con.prepareStatement("SELECT s.sid, s.tid, s.mid FROM CINEMA_SESSION AS s");
            ResultSet rs = stmt.executeQuery();
            int size=0;
            while (rs.next()) size++;

            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1+16:00&mid="+movId[2]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1+20:30&mid="+movId[0]}, cmdUtils, ds).executeCommand(con);

            stmt = con.prepareStatement("SELECT s.sid, s.tid, s.mid FROM CINEMA_SESSION AS s");
            rs = stmt.executeQuery();
            int sizeAfter=0;
            while (rs.next()) sizeAfter++;
            assertEquals(size,sizeAfter);

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
    public void get_sessions_by_cinema() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            DataContainer data = new CommandRequest(new String[]{"GET", "/cinemas/"+cinemaId+"/sessions"}, cmdUtils, ds).executeCommand(con);
            LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
            Session session;

            for (int i = 0; i < sessions.size(); i++) {
                session = sessions.get(i);
                assertEquals(sessionsId[i], session.getId());
                assertEquals(cinemaId, session.getCinemaID());
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
    public void get_sessions_by_theater() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            for(int t=0; t<theatersId.length; t++) {
                DataContainer data = new CommandRequest(new String[]{"GET", "/cinemas/"+cinemaId+"/theaters/" + theatersId[t] + "/sessions"}, cmdUtils, ds).executeCommand(con);

                String tid = (String) data.getData(D_TID);
                assertEquals(theatersId[t], tid);
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
    public void get_session_by_id() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA_SESSION");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id=rs.getString(1);
                DataContainer data = new CommandRequest(new String[]{"GET", "/cinemas/" + cinemaId + "/sessions/" + id}, cmdUtils, ds).executeCommand(con);
                Session session = (Session) data.getData(D_SESSION);
                assertEquals(id, session.getId());
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
    public void get_sessions_today_by_cinema(){
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            DataContainer data = new CommandRequest(new String[]{"GET", "/cinemas/"+cinemaId+"/sessions/today"}, cmdUtils, ds).executeCommand(con);
            LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
            Session session;
            for (int i = 0; i < sessions.size(); i++) {
                session = sessions.get(i);
                assertEquals(cinemaId, session.getCinemaID());
                assertEquals(date, session.getDateTime().split(" ")[0]);
                i+=4;
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
    public void get_sessions_today_by_theater(){
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            for(int t=0;t<theatersId.length;t++) {
                DataContainer data = new CommandRequest(new String[]{"GET", "/cinemas/"+cinemaId+"/theaters/" + theatersId[t] + "/sessions/today"}, cmdUtils, ds).executeCommand(con);
                LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
                Session session;
                String tid = (String) data.getData(D_TID);
                assertEquals(theatersId[t], tid);
                for (int i = 0; i < sessions.size(); i++) {
                    session = sessions.get(i);
                    assertEquals(date, session.getDateTime().split(" ")[0]);
                }
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
    public void get_movie_session_on_date(){
        try{
            con = ds.getConnection();
            con.setAutoCommit(false);
            createSession(con);

            int available=12*18, cid=1, idx =0;
            String city="Lisboa";
            String date= "2018-04-01";
            Session session;
            DataContainer data;
            LinkedList<Session> sessions;

            data = new CommandRequest(new String[] {"GET", "/movies/"+movId[idx] +"/sessions/date/01042018","available="+available}, cmdUtils, ds).executeCommand(con);
            sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
            String mid = (String) data.getData(D_MID);
            assertEquals(movId[idx], mid);
            for (int i = 0; i < sessions.size(); i++) {
                session = sessions.get(i);
                assertEquals(date, session.getDateTime().split(" ")[0]);
                assertEquals(available, session.getAvailableSeats());
            }

            data = new CommandRequest(new String[]{"GET", "/movies/"+movId[idx] +"/sessions/date/01042018","cid="+cid}, cmdUtils, ds).executeCommand(con);
            sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
            mid = (String) data.getData(D_MID);
            assertEquals(movId[idx], mid);
            for (int i = 0; i < sessions.size(); i++) {
                session = sessions.get(i);
                assertEquals(date, session.getDateTime().split(" ")[0]);
                assertEquals(String.valueOf(cid), session.getCinemaID());
            }

            PreparedStatement stmt;
            ResultSet rs;
            data = new CommandRequest(new String[]{"GET", "/movies/"+movId[idx]+"/sessions/date/01042018","city="+city}, cmdUtils, ds).executeCommand(con);
            sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
            mid = (String) data.getData(D_MID);
            assertEquals(movId[idx], mid);
            for (int i = 0; i < sessions.size(); i++) {
                session = sessions.get(i);
                assertEquals(date, session.getDateTime().split(" ")[0]);
                stmt = con.prepareStatement("SELECT DISTINCT c.City FROM CINEMA WHERE c.cid=" + session.getCinemaID());
                rs = stmt.executeQuery();
                assertEquals(city, rs.getString(1));
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
    public void get_cinema_session_on_date(){
        try{
            con = ds.getConnection();
            con.setAutoCommit(false);
            createSession(con);
            String date= "2018-04-01";
            DataContainer data = new CommandRequest(new String[]{"GET", "/cinemas/"+cinemaId+"/sessions/date/01042018"}, cmdUtils, ds).executeCommand(con);
            LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
            Session session;
            for (int i = 0; i < sessions.size(); i++) {
                session = sessions.get(i);
                assertEquals(cinemaId, session.getCinemaID());
                assertEquals(date, session.getDateTime().split(" ")[0]);
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
}