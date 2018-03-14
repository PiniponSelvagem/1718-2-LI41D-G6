package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class PostCinemaIDTheaters implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException {
        /*int seats = Integer.parseInt(cmdBuilder.getParameter(String.valueOf(ROWS))) *
                    Integer.parseInt(cmdBuilder.getParameter(String.valueOf(SEATS_ROW)));

        return "INSERT INTO THEATER VALUES ("+
                seats+","+
                cmdBuilder.getParameter(String.valueOf(SEATS_ROW))+","+
                cmdBuilder.getParameter(String.valueOf(ROWS))+","+
                cmdBuilder.getParameter(String.valueOf(NAME))+","+
                cmdBuilder.getParameter(String.valueOf(CINEMAS_ID))+");";
                */
    }

    @Override //malfunction with parameters
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO THEATER VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, cmdBuilder.getParameter((String.valueOf(0))));//will be available seats
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(SEATS_ROW))));
        stmt.setString(3, cmdBuilder.getParameter((String.valueOf(ROWS))));
        stmt.setString(4, cmdBuilder.getParameter((String.valueOf(NAME))));
        stmt.setString(5, cmdBuilder.getParameter((String.valueOf(CINEMAS_ID))));
        stmt.execute();
        System.out.println("Posted theater");
    }
}
