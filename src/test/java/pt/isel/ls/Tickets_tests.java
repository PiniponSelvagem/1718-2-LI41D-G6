package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.sql.Sql;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketIDView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketsAvailableView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketsView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class Tickets_tests {

    private Connection con = null;
    int cinemaId;
    int []theatersId= new int[2], movId= new int[3], sessionsId= new int[6];
    String[] ticketsId= new String[8];

    public void createTickets(Connection con) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String date= dtf.format(localDate);
            for (int i = 0; i < 3; i++) {
                String title = "TestTitle";
                title += (i + 1);
                new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/movies", "title=" + title + "&releaseYear=2000&duration=90"}, new CommandUtils()), con, false);
            }
            PreparedStatement stmt = con.prepareStatement("SELECT mid FROM MOVIE");
            ResultSet rs = stmt.executeQuery();
            int i=0;
            while(rs.next() && i<movId.length) {
                movId[i] = rs.getInt(1);
                i++;
            }
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas", "name=cinema1&city=cidade1"}, new CommandUtils()), con, false);
            stmt = con.prepareStatement("SELECT cid FROM CINEMA");
            rs = stmt.executeQuery();
            if(rs.next()) cinemaId = rs.getInt(1);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters", "name=sala1&row=12&seat=18"}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters", "name=sala2&row=12&seat=18"}, new CommandUtils()), con, false);
            stmt = con.prepareStatement("SELECT tid FROM THEATER");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<theatersId.length) {
                theatersId[i] = rs.getInt(1);
                i++;
            }
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+String.valueOf(theatersId[0])+"/sessions", "date=2018/4/1+12:00&mid="+movId[0]}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date="+date+"+19:00&mid="+movId[1]}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[0]+"/sessions", "date=2018/4/1+15:30&mid="+movId[2]}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1+12:00&mid="+movId[0]}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date=2018/4/1+15:30&mid="+movId[1]}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/"+cinemaId+"/theaters/"+theatersId[1]+"/sessions", "date="+date+"+19:00&mid="+movId[2]}, new CommandUtils()), con, false);
            stmt = con.prepareStatement("SELECT sid FROM CINEMA_SESSION");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<sessionsId.length) {
                sessionsId[i] = rs.getInt(1);
                i++;
            }
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=1&row=A"}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[1]+"/tickets","seat=2&row=B"}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[2]+"/tickets","seat=3&row=C"}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[1] + "/sessions/" + sessionsId[3]+"/tickets","seat=4&row=D"}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[1] + "/sessions/" + sessionsId[4]+"/tickets","seat=5&row=E"}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[1] + "/sessions/" + sessionsId[5]+"/tickets","seat=6&row=F"}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[1]+"/tickets","seat=3&row=B"}, new CommandUtils()), con, false);
            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[1] + "/sessions/" + sessionsId[4]+"/tickets","seat=6&row=E"}, new CommandUtils()), con, false);

            stmt = con.prepareStatement("SELECT tkid FROM TICKET");
            rs = stmt.executeQuery();
            i=0;
            while(rs.next() && i<ticketsId.length) {
                ticketsId[i] = rs.getString(1);
                i++;
            }
        } catch (SQLException | CommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insert_tickets() {
        try {
            con = Sql.getConnection();
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
            con = Sql.getConnection();
            con.setAutoCommit(false);
            createTickets(con);
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT tk.tkid, tk.sid, s.tid , t.cid FROM TICKET AS tk " +
                            "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                            "INNER JOIN THEATER AS t ON t.tid=s.tid "
            );
            ResultSet rs = stmt.executeQuery();
            GetCinemaIDTheaterIDSessionIDTicketsView view;
            DataContainer data;
            Ticket ticket;
            while (rs.next()) {
                view=(GetCinemaIDTheaterIDSessionIDTicketsView) new CommandRequest().executeCommand(new CommandBuilder(new String[]{"GET", "/cinemas/" + rs.getInt(4) +"/theaters/"+rs.getInt(3)+"/sessions/" + rs.getString(2)+"/tickets"}, new CommandUtils()), con, false);
                data=view.getData();
                LinkedList<Ticket> tickets = (LinkedList<Ticket>) data.getData(D_TICKETS);
                for(int i=0; i<tickets.size() ;i++) {
                    ticket = tickets.get(i);
                    assertEquals(rs.getInt(2), ticket.getSession().getId());
                    assertEquals(rs.getInt(3), ticket.getSession().getTheater().getId());
                    assertEquals(rs.getInt(4), ticket.getSession().getCinemaID());
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
    public void get_thickets_by_id(){
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            createTickets(con);
            PreparedStatement stmt = con.prepareStatement(
                        "SELECT tk.tkid, tk.sid, s.tid , t.cid FROM TICKET AS tk " +
                            "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                            "INNER JOIN THEATER AS t ON t.tid=s.tid "
            );
            ResultSet rs = stmt.executeQuery();
            GetCinemaIDTheaterIDSessionIDTicketIDView sessionIDView;
            DataContainer data;
            Ticket ticket;
            while (rs.next()) {
                sessionIDView = (GetCinemaIDTheaterIDSessionIDTicketIDView) new CommandRequest().executeCommand(new CommandBuilder(new String[]{"GET", "/cinemas/" + rs.getInt(4) +"/theaters/"+rs.getInt(3)+"/sessions/" + rs.getString(2)+"/tickets/" + rs.getString(1)}, new CommandUtils()), con, false);
                data = sessionIDView.getData();
                ticket = (Ticket) data.getData(D_TICKET);
                assertEquals(rs.getString(1), ticket.getId());
                assertEquals(rs.getInt(2),ticket.getSession().getId());
                assertEquals(rs.getInt(3),ticket.getSession().getTheater().getId());
                assertEquals(rs.getInt(4),ticket.getSession().getCinemaID());
            }

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

    @Test
    public void get_available_tickets() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            createTickets(con);

            GetCinemaIDTheaterIDSessionIDTicketsAvailableView view = (GetCinemaIDTheaterIDSessionIDTicketsAvailableView) new CommandRequest().executeCommand(new CommandBuilder(new String[]{"GET", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[1]+"/tickets/available"}, new CommandUtils()), con, false);
            DataContainer data = view.getData();
            int availableSeats = (Integer) data.getData(D_AVAILABLE_SEATS);
            assertEquals((18*12)-2, availableSeats);
            view = (GetCinemaIDTheaterIDSessionIDTicketsAvailableView) new CommandRequest().executeCommand(new CommandBuilder(new String[]{"GET", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets/available"}, new CommandUtils()), con, false);
            data = view.getData();
            availableSeats = (Integer) data.getData(D_AVAILABLE_SEATS);
            assertEquals((18*12)-1, availableSeats);

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
    public void post_wrong_tickets() {
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            createTickets(con);

            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=19&row=A"}, new CommandUtils()), con, false);
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT tk.tkid FROM TICKET AS tk " +
                            "WHERE tk.tkid=? AND tk.sid=? "
            );
            stmt.setString(1,"A19");
            stmt.setInt(2,sessionsId[0]);
            ResultSet rs = stmt.executeQuery();
            assertFalse(rs.next());

            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=0&row=A"}, new CommandUtils()), con, false);
            stmt = con.prepareStatement(
                    "SELECT tk.tkid FROM TICKET AS tk " +
                            "WHERE tk.tkid=? AND tk.sid=? "
            );
            stmt.setString(1,"A0");
            stmt.setInt(2,sessionsId[0]);
            rs = stmt.executeQuery();
            assertFalse(rs.next());

            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=17&row=M"}, new CommandUtils()), con, false);
            stmt = con.prepareStatement(
                    "SELECT tk.tkid FROM TICKET AS tk " +
                            "WHERE tk.tkid=? AND tk.sid=? "
            );
            stmt.setString(1,"M17");
            stmt.setInt(2,sessionsId[0]);
            rs = stmt.executeQuery();
            assertFalse(rs.next());

            new CommandRequest().executeCommand(new CommandBuilder(new String[]{"POST", "/cinemas/" + cinemaId + "/theaters/" + theatersId[0] + "/sessions/" + sessionsId[0]+"/tickets","seat=19&row=M"}, new CommandUtils()), con, false);
            stmt = con.prepareStatement(
                    "SELECT tk.tkid FROM TICKET AS tk " +
                            "WHERE tk.tkid=? AND tk.sid=? "
            );
            stmt.setString(1,"L19");
            stmt.setInt(2,sessionsId[0]);
            rs = stmt.executeQuery();
            assertFalse(rs.next());

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
