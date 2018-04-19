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
        String id = cmdBuilder.getParameter((String.valueOf(ROWS))) + cmdBuilder.getParameter((String.valueOf(SEATS_ROW)));
        stmt.setString(1, id);
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(SEATS_ROW))));
        stmt.setString(3, cmdBuilder.getParameter((String.valueOf(ROWS))));
        stmt.setString(4, cmdBuilder.getId((String.valueOf(SESSION_ID))));
        stmt.executeUpdate();

        stmt = connection.prepareStatement("UPDATE THEATER SET SeatsAvailable = SeatsAvailable - 1 " +
                                                "FROM THEATER INNER JOIN CINEMA_SESSION ON THEATER.tid = CINEMA_SESSION.tid " +
                                                "INNER JOIN TICKET ON TICKET.sid = CINEMA_SESSION.sid " +
                                                "WHERE TICKET.tkid=?"
        );
        stmt.setString(1, id);
        stmt.executeUpdate();

        return new PostView<>("Ticket ID: ", id);
    }
}
