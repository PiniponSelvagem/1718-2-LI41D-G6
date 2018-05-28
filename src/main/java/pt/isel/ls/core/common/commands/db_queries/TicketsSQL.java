package pt.isel.ls.core.common.commands.db_queries;

import pt.isel.ls.model.Ticket;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataEnum.PD_FAILED;
import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataEnum.PD_OK;
import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataType.PDT_TICKET;

public class TicketsSQL {

    /**
     * Post ticket
     * @param con SQL Connection
     * @param sessionID sessionID
     * @param row row letter of the seat
     * @param seat seat number
     * @return Returns id of posted ticket
     * @throws SQLException SQLException
     */
    public static PostData postTicket(Connection con, int sessionID, String row, int seat) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT t.Seats, t.Rows " +
                "FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "WHERE s.sid=?"
        );
        stmt.setInt(1, sessionID);
        ResultSet rs = stmt.executeQuery();
        int seats=0, rows=0;
        int rowValue = row.charAt(0)-'A'+1;
        if(rs.next()) {
            seats = rs.getInt(1);
            rows=rs.getInt(2);
        }
        if(seat<=seats && rowValue<=rows && seat>0 && rowValue>0) {
            stmt = con.prepareStatement("INSERT INTO TICKET VALUES(?, ?, ?, ?)");
            String id = row+seat;
            stmt.setString(1, id);
            stmt.setInt(2, seat);
            stmt.setString(3, row);
            stmt.setInt(4, sessionID);
            stmt.executeUpdate();

            stmt = con.prepareStatement(
                    "UPDATE CINEMA_SESSION SET CINEMA_SESSION.SeatsAvailable = CINEMA_SESSION.SeatsAvailable - 1 "+
                    "WHERE CINEMA_SESSION.sid=?"
            );
            stmt.setInt(1, sessionID);
            stmt.executeUpdate();
            return new PostData(PD_OK, PDT_TICKET, id);
        }
        return new PostData(PD_FAILED);
    }

    /**
     * Delete tickets
     * @param con SQL Connection
     * @param sessionID sesionID
     * @param ticketsIDs ticketsIDs, list to be able to delete multiple tickets in one go
     * @param numberOfTickets numberOfTickets to be deleted
     * @return Returns true if success.
     * @throws SQLException SQLException
     */
    public static boolean deleteTicket(Connection con, int sessionID, List<String> ticketsIDs, int numberOfTickets) throws SQLException {
        StringBuilder sql = new StringBuilder();

        int i = 0;
        while (i < numberOfTickets) {
            sql.append("tkid=").append("'").append(ticketsIDs.get(i++)).append("'");
            if(i < numberOfTickets) sql.append(" OR ");
        }

        PreparedStatement stmt = con.prepareStatement(
                "DELETE FROM TICKET "+
                "WHERE TICKET.sid=? AND (" + sql.toString()+")"
        );
        stmt.setInt(1, sessionID);

        int test = stmt.executeUpdate();
        while(numberOfTickets>0) {
            stmt = con.prepareStatement(
                    "UPDATE CINEMA_SESSION SET CINEMA_SESSION.SeatsAvailable = CINEMA_SESSION.SeatsAvailable + 1 " +
                    "WHERE CINEMA_SESSION.sid=?"
            );
            stmt.setInt(1, sessionID);
            stmt.executeUpdate();
            --numberOfTickets;
        }
        return test!=0;
    }

    /**
     * Get all tickets and place them in a hashmap.
     * @param con SQL Connection
     * @param sessionID sessionID
     * @return Returns list of tickets
     * @throws SQLException SQLException
     */
    public static Map<String, Ticket> queryAll(Connection con, int sessionID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT tk.row, tk.seat, s.sid " +
                "FROM TICKET AS tk " +
                "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                "WHERE s.sid=? " +
                "ORDER BY tk.row, tk.seat"
        );
        stmt.setInt(1, sessionID);
        ResultSet rs = stmt.executeQuery();

        HashMap<String, Ticket> tickets = new HashMap<>();
        Ticket ticket;
        while(rs.next()){
            ticket = new Ticket(rs.getString(1).charAt(0), rs.getInt(2), rs.getInt(3));
            tickets.put(ticket.getId(), ticket);
        }

        return tickets;
    }

    /**
     * Get the ticket with requested ID
     * @param con SQL Connection
     * @param sessionID sessionID
     * @param ticketID ticketID
     * @return Returns Ticket
     * @throws SQLException SQLException
     */
    public static Ticket queryID(Connection con, int sessionID, String ticketID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT tk.row, tk.seat, s.sid " +
                "FROM TICKET AS tk " +
                "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                "WHERE tk.tkid=? AND tk.sid=?"
        );
        stmt.setString(1, ticketID);
        stmt.setInt(2, sessionID);
        ResultSet rs = stmt.executeQuery();

        if(!rs.next())
            return null;

        return new Ticket(rs.getString(1).charAt(0), rs.getInt(2), rs.getInt(3));
    }
}
