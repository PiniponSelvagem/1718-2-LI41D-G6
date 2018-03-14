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
        String seats = String.valueOf(
                Integer.parseInt(cmdBuilder.getParameter(String.valueOf(ROWS)))
                * Integer.parseInt(cmdBuilder.getParameter(String.valueOf(SEATS_ROW)))
        );
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO THEATER VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, seats);
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(SEATS_ROW))));
        stmt.setString(3, cmdBuilder.getParameter((String.valueOf(ROWS))));
        stmt.setString(4, cmdBuilder.getParameter((String.valueOf(NAME))));
        stmt.setString(5, String.valueOf(cmdBuilder.popId()));
        stmt.execute();
        System.out.println("Posted theater");
    }
}
