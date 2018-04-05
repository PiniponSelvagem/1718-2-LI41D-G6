package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class PostCinemaIDTheaterIDSessionIDTickets extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, SQLException {

        String id = "";
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO TICKET VALUES(?, ?, ?, ?)");
        id = cmdBuilder.getParameter((String.valueOf(ROWS))) + cmdBuilder.getParameter((String.valueOf(SEATS_ROW)));
        stmt.setString(1, id);
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(SEATS_ROW))));
        stmt.setString(3, cmdBuilder.getParameter((String.valueOf(ROWS))));
        stmt.setString(4, cmdBuilder.getId((String.valueOf(SESSION_ID))));
        stmt.execute();

        return new PostView<>("Ticket ID: ", id);
    }
}
