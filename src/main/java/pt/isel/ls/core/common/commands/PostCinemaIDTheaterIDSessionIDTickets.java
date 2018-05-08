package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;


public class PostCinemaIDTheaterIDSessionIDTickets extends Command {

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX
                +DIR_SEPARATOR+TICKETS;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        PreparedStatement stmt=connection.prepareStatement("SELECT t.Seats, t.Rows " +
                "FROM CINEMA_SESSION AS s INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "WHERE s.sid=?"
        );
        stmt.setString(1, cmdBuilder.getId(SESSION_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        int seats=0, rows=0;
        int seat=Integer.parseInt(cmdBuilder.getParameter(SEATS_ROW.toString()));
        int row=cmdBuilder.getParameter(ROWS.toString()).charAt(0)-'A'+1;
        if(rs.next()) {
            seats = rs.getInt(1);
            rows=rs.getInt(2);
        }
        if(seat<=seats && row<=rows && seat>0 && row>0) {
            stmt = connection.prepareStatement("INSERT INTO TICKET VALUES(?, ?, ?, ?)");
            String id = cmdBuilder.getParameter(ROWS.toString()) + cmdBuilder.getParameter(SEATS_ROW.toString());
            stmt.setString(1, id);
            stmt.setString(2, cmdBuilder.getParameter(SEATS_ROW.toString()));
            stmt.setString(3, cmdBuilder.getParameter(ROWS.toString()));
            stmt.setString(4, cmdBuilder.getId(SESSION_ID.toString()));
            stmt.executeUpdate();

            stmt = connection.prepareStatement("UPDATE CINEMA_SESSION SET CINEMA_SESSION.SeatsAvailable = CINEMA_SESSION.SeatsAvailable - 1 "+
                    "WHERE CINEMA_SESSION.sid = ?"
            );
            stmt.setString(1, cmdBuilder.getId(SESSION_ID.toString()));
            stmt.executeUpdate();
            return new PostView<>("Ticket ID: ", id);
        }
        return new PostView<>("Ticket ", "NOT POSTED!");
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
