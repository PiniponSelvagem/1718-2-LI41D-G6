package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;


public class PostCinemaIDTheaterIDSessionIDTickets extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO TICKET VALUES(?, ?, ?, ?)");
        String id = cmdBuilder.getParameter(ROWS.toString()) + cmdBuilder.getParameter(SEATS_ROW.toString());
        stmt.setString(1, id);
        stmt.setString(2, cmdBuilder.getParameter(SEATS_ROW.toString()));
        stmt.setString(3, cmdBuilder.getParameter(ROWS.toString()));
        stmt.setString(4, cmdBuilder.getId(SESSION_ID.toString()));
        stmt.executeUpdate();

        stmt = connection.prepareStatement("update SEATS SET SEATS.seats = SEATS.seats - 1 from SEATS inner join THEATER on THEATER.tid = Seats.tid " +
                                            "inner join CINEMA_SESSION on CINEMA_SESSION.sid = SEATS.sid " +
                                            "WHERE THEATER.tid = ? and CINEMA_SESSION.sid = ?"
        );
        stmt.setString(1, cmdBuilder.getId(THEATER_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(SESSION_ID.toString()));
        stmt.executeUpdate();
        return new PostView<>("Ticket ID: ", id);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
