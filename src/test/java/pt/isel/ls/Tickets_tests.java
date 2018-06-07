package pt.isel.ls;

import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.exceptions.CommonException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandRequest;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.sql.Sql;
import pt.isel.ls.view.json.GetCinemaIDTheaterIDSessionIDTicketIDView;
import pt.isel.ls.view.json.GetCinemaIDTheaterIDSessionIDTicketsAvailableView;
import pt.isel.ls.view.json.GetCinemaIDTheaterIDSessionIDTicketsView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static pt.isel.ls.SqlTest.CreateConnetion;
import static pt.isel.ls.core.common.headers.HeadersAvailable.TEXT_HTML;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

@SuppressWarnings("Duplicates")
public class Tickets_tests {
    private static final PGSimpleDataSource ds = CreateConnetion();
    private final static CommandUtils cmdUtils = new CommandUtils(TEXT_HTML.toString());
    private Connection con = null;
    String cinemaId;
    String []theatersId= new String[2], movId= new String[3], sessionsId= new String[6];
    String[] ticketsId= new String[8];

    public void createTickets(Connection con) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            for (int i = 0; i < 3; i++) {
                String title = "TestTitle";
                title += (i + 1);
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
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+String.valueOf(theatersId[0])+"/sessions", "date=2018/4/1+12:00&mid="+movId[0]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date="+date+"+19:00&mid="+movId[1]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1+15:30&mid="+movId[2]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1+12:00&mid="+movId[0]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1+15:30&mid="+movId[1]}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date="+date+"+19:00&mid="+movId[2]}, cmdUtils, ds).executeCommand(con);
            stmt = con.prepareStatement("SELECT sid FROM CINEMA_SESSION");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<sessionsId.length) {
                sessionsId[i] = rs.getString(1);
                i++;
            }
            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=1&row=A"}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[1]+"/tickets","seat=2&row=B"}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[2]+"/tickets","seat=3&row=C"}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[1] + "/sessions/" + sessionsId[3]+"/tickets","seat=4&row=D"}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[1] + "/sessions/" + sessionsId[4]+"/tickets","seat=5&row=E"}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[1] + "/sessions/" + sessionsId[5]+"/tickets","seat=6&row=F"}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[1]+"/tickets","seat=3&row=B"}, cmdUtils, ds).executeCommand(con);
            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[1] + "/sessions/" + sessionsId[4]+"/tickets","seat=6&row=E"}, cmdUtils, ds).executeCommand(con);

            stmt = con.prepareStatement("SELECT tkid FROM TICKET");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<ticketsId.length) {
                ticketsId[i] = rs.getString(1);
                i++;
            }
        } catch (SQLException | CommonException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insert_tickets() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            createTickets(con);
            PreparedStatement stmt = con.prepareStatement("SELECT tk.tkid, tk.sid, t.Rows, t.Seats FROM TICKET AS tk " +
                    "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid INNER JOIN THEATER AS t ON t.tid=s.tid");
            ResultSet rs = stmt.executeQuery();
            int i=0;
            while (rs.next()) {
                assertEquals(rs.getString(1),ticketsId[i]);
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
    public void get_tickets() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            createTickets(con);
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT tk.tkid, tk.sid, s.tid , t.cid FROM TICKET AS tk " +
                            "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                            "INNER JOIN THEATER AS t ON t.tid=s.tid "
            );
            ResultSet rs = stmt.executeQuery();
            DataContainer data;
            Ticket ticket;
            while (rs.next()) {
                data =  new CommandRequest(new String[]{"GET", "/cinemas/" + rs.getInt(4) +"/theaters/"+rs.getInt(3)+"/sessions/" + rs.getString(2)+"/tickets"}, cmdUtils, ds).executeCommand(con);
                LinkedList<Ticket> tickets = (LinkedList<Ticket>) data.getData(D_TICKETS);
                String cid = (String) data.getData(D_CID);
                Theater theater = (Theater) data.getData(D_THEATER);
                assertEquals(rs.getString(4), cid);
                assertEquals(rs.getString(3), theater.getId());
                for(int i=0; i<tickets.size(); i++) {
                    ticket = tickets.get(i);
                    assertEquals(rs.getString(2), ticket.getSessionID());
                }
            }
        } catch (SQLException | CommonException e) {
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
    public void get_thickets_by_id(){
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            createTickets(con);
            PreparedStatement stmt = con.prepareStatement(
                        "SELECT tk.tkid, tk.sid, s.tid , t.cid FROM TICKET AS tk " +
                            "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                            "INNER JOIN THEATER AS t ON t.tid=s.tid "
            );
            ResultSet rs = stmt.executeQuery();
            DataContainer data;
            Ticket ticket;
            Cinema cinema;
            Theater theater;
            while (rs.next()) {
                data = new CommandRequest(new String[]{"GET", "/cinemas/" + rs.getInt(4) +"/theaters/"+rs.getInt(3)+"/sessions/" + rs.getString(2)+"/tickets/" + rs.getString(1)}, cmdUtils, ds).executeCommand(con);
                ticket = (Ticket) data.getData(D_TICKET);
                cinema = (Cinema) data.getData(D_CINEMA);
                theater = (Theater) data.getData(D_THEATER);
                assertEquals(rs.getString(1), ticket.getId());
                assertEquals(rs.getString(2), ticket.getSessionID());
                assertEquals(rs.getString(3), theater.getId());
                assertEquals(rs.getString(4), cinema.getId());
            }

        } catch (SQLException | CommonException e) {
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

    @Test
    public void get_available_tickets() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            createTickets(con);

            DataContainer data = new CommandRequest(new String[]{"GET", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[1]+"/tickets/available"}, cmdUtils, ds).executeCommand(con);
            int availableSeats = (Integer) data.getData(D_AVAILABLE_SEATS);
            assertEquals((18*12)-2, availableSeats);
            data = new CommandRequest(new String[]{"GET", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets/available"}, cmdUtils, ds).executeCommand(con);
            availableSeats = (Integer) data.getData(D_AVAILABLE_SEATS);
            assertEquals((18*12)-1, availableSeats);

        } catch (SQLException | CommonException e) {
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
    public void post_wrong_tickets() {
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            createTickets(con);

            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=19&row=A"}, cmdUtils, ds).executeCommand(con);
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT tk.tkid FROM TICKET AS tk " +
                            "WHERE tk.tkid=? AND tk.sid=? "
            );
            stmt.setString(1,"A19");
            stmt.setInt(2, Integer.parseInt(sessionsId[0]));
            ResultSet rs = stmt.executeQuery();
            assertFalse(rs.next());

            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=0&row=A"}, cmdUtils, ds).executeCommand(con);
            stmt = con.prepareStatement(
                    "SELECT tk.tkid FROM TICKET AS tk " +
                            "WHERE tk.tkid=? AND tk.sid=? "
            );
            stmt.setString(1,"A0");
            stmt.setInt(2, Integer.parseInt(sessionsId[0]));
            rs = stmt.executeQuery();
            assertFalse(rs.next());

            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=17&row=M"}, cmdUtils, ds).executeCommand(con);
            stmt = con.prepareStatement(
                    "SELECT tk.tkid FROM TICKET AS tk " +
                            "WHERE tk.tkid=? AND tk.sid=? "
            );
            stmt.setString(1,"M17");
            stmt.setInt(2, Integer.parseInt(sessionsId[0]));
            rs = stmt.executeQuery();
            assertFalse(rs.next());

            new CommandRequest(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=19&row=M"}, cmdUtils, ds).executeCommand(con);
            stmt = con.prepareStatement(
                    "SELECT tk.tkid FROM TICKET AS tk " +
                            "WHERE tk.tkid=? AND tk.sid=? "
            );
            stmt.setString(1,"L19");
            stmt.setInt(2, Integer.parseInt(sessionsId[0]));
            rs = stmt.executeQuery();
            assertFalse(rs.next());

        } catch (SQLException | CommonException e) {
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
