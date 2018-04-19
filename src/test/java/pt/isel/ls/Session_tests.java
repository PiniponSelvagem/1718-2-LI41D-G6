package pt.isel.ls;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
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
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters", "name=sala1&row=12&seat=18"}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters", "name=sala2&row=12&seat=18"}, new CommandUtils()));
            stmt = con.prepareStatement("SELECT tid FROM THEATER");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<theatersId.length) {
                theatersId[i] = rs.getInt(1);
                i++;
            }
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1+12:00&mid="+movId[0]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date="+date+"+19:00&mid="+movId[1]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1+15:30&mid="+movId[2]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1+12:00&mid="+movId[0]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1+15:30&mid="+movId[1]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date="+date+"+19:00&mid="+movId[2]}, new CommandUtils()));
            stmt = con.prepareStatement("SELECT sid FROM CINEMA_SESSION");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<sessionsId.length) {
                sessionsId[i] = rs.getInt(1);
                i++;
            }
        } catch (SQLException | CommandException e) {
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
    public void session_not_posted(){
        try{
            con = Sql.getConnection();
            con.setAutoCommit(false);
            createSession(con);

            PreparedStatement stmt = con.prepareStatement("SELECT s.sid, s.tid, s.mid FROM CINEMA_SESSION AS s");
            ResultSet rs = stmt.executeQuery();
            int size=0;
            while (rs.next()) size++;

            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1+16:00&mid="+movId[2]}, new CommandUtils()));
            Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1+12:30&mid="+movId[0]}, new CommandUtils()));

            stmt = con.prepareStatement("SELECT s.sid, s.tid, s.mid FROM CINEMA_SESSION AS s");
            rs = stmt.executeQuery();
            int sizeAfter=0;
            while (rs.next()) sizeAfter++;
            assertEquals(size,sizeAfter);

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
    public void get_sessions_by_cinema() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            GetCinemaIDSessionsView view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/sessions"}, new CommandUtils()));
            DataContainer data = view.getData();
            Session session;

            for (int i = 0; i < data.size(); i++) {
                session = (Session) data.getData(i);
                assertEquals(sessionsId[i], session.getId());
                assertEquals(cinemaId, session.getCinemaID());
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
    public void get_sessions_by_theater() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            for(int t=0; t<theatersId.length; t++) {
                GetCinemaIDTheaterIDSessionsView view = (GetCinemaIDTheaterIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/theaters/" + theatersId[t] + "/sessions"}, new CommandUtils()));
                DataContainer data = view.getData();
                Session session;
                for (int i = 0; i < data.size(); i++) {
                    session = (Session) data.getData(i);
                    assertEquals(theatersId[t], session.getTheater().getId());
                }
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
    public void get_session_by_id() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM CINEMA_SESSION");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id=rs.getInt(1);
                GetCinemaIDSessionIDView sessionIDView = (GetCinemaIDSessionIDView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/" + cinemaId + "/sessions/" + id}, new CommandUtils()));
                DataContainer data = sessionIDView.getData();
                Session session = (Session)data.getData(0);
                assertEquals(id, session.getId());
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
    public void get_sessions_today_by_cinema(){
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            GetCinemaIDSessionsView view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/sessions/today"}, new CommandUtils()));
            DataContainer data = view.getData();
            Session session;
            for (int i = 0; i < data.size(); i++) {
                session = (Session) data.getData(i);
                assertEquals(cinemaId, session.getCinemaID());
                assertEquals(date, session.getDateTime().split(" ")[0]);
                i+=4;
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
    public void get_sessions_today_by_theater(){
		//line 255 core never identified
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);

            createSession(con);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            for(int t=0;t<theatersId.length;t++) {
                GetCinemaIDTheaterIDSessionsView view = (GetCinemaIDTheaterIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/theaters/" + theatersId[t] + "/sessions/today"}, new CommandUtils()));
                DataContainer data = view.getData();
                Session session;
                for (int i = 0; i < data.size(); i++) {
                    session = (Session) data.getData(i);
                    assertEquals(theatersId[t], session.getTheater().getId());
                    assertEquals(date, session.getDateTime().split(" ")[0]);
                }
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
    public void get_movie_session_on_date(){
        try{
            con = Sql.getConnection();
            con.setAutoCommit(false);
            createSession(con);
            int available=12*18, cid=1, idx =0;
            String city="Lisboa";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String localDate= "01/04/2018";
            String date= dtf.format(dtf.parse(localDate));
            GetCinemaIDSessionsView view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/movies/"+movId[idx] +"/sessions/01042018 available="+available}, new CommandUtils()));
            DataContainer data = view.getData();
            Session session;
            for (int i = 0; i < data.size(); i++) {
                session = (Session) data.getData(i);
                assertEquals(movId[idx], session.getMovie().getId());
                assertEquals(date, session.getDateTime().split(" ")[0]);
                assertEquals(available, session.getTheater().getAvailableSeats());
            }

            view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/movies/"+movId[idx] +"/sessions/01042018 cid="+cid}, new CommandUtils()));
            data = view.getData();
            for (int i = 0; i < data.size(); i++) {
                session = (Session) data.getData(i);
                assertEquals(movId[idx], session.getMovie().getId());
                assertEquals(date, session.getDateTime().split(" ")[0]);
                assertEquals(cid, session.getCinemaID());
            }
            PreparedStatement stmt;
            ResultSet rs;
            view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/movies/"+movId[idx] +"/sessions/01042018 city="+city}, new CommandUtils()));
            data = view.getData();
            for (int i = 0; i < data.size(); i++) {
                session = (Session) data.getData(i);
                assertEquals(movId[idx], session.getMovie().getId());
                assertEquals(date, session.getDateTime().split(" ")[0]);
                stmt = con.prepareStatement("SELECT DISTINCT c.City FROM CINEMA WHERE c.cid="+session.getCinemaID());
                rs = stmt.executeQuery();
                assertEquals(city,rs.getString(1));
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
    public void get_cinema_session_on_date(){
        try{
            con = Sql.getConnection();
            con.setAutoCommit(false);
            createSession(con);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String localDate= "01/04/2018";
            String date= dtf.format(dtf.parse(localDate));
            GetCinemaIDSessionsView view = (GetCinemaIDSessionsView) Main.executeBuildedCommand(con, new CommandBuilder(new String[]{"GET", "/cinemas/"+cinemaId+"/sessions/01042018"}, new CommandUtils()));
            DataContainer data = view.getData();
            Session session;
            for (int i = 0; i < data.size(); i++) {
                session = (Session) data.getData(i);
                assertEquals(cinemaId, session.getCinemaID());
                assertEquals(date, session.getDateTime().split(" ")[0]);
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

}