package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class PostCinemaIDTheaters implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, SQLException {
        int seats = Integer.parseInt(cmdBuilder.getParameter(String.valueOf(ROWS)))
                * Integer.parseInt(cmdBuilder.getParameter(String.valueOf(SEATS_ROW)));
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO THEATER VALUES (?, ?, ?, ?, ?)");
        stmt.setInt(1, seats);
        stmt.setInt(2, Integer.parseInt(cmdBuilder.getParameter((String.valueOf(ROWS)))));
        stmt.setInt(3, Integer.parseInt(cmdBuilder.getParameter((String.valueOf(SEATS_ROW)))));
        stmt.setString(4, cmdBuilder.getParameter((String.valueOf(NAME))));
        stmt.setInt(5, Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))));
        stmt.execute();
        System.out.println("Posted theater");
    }
}
