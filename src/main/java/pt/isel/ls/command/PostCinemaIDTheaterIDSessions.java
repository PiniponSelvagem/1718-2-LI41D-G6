package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.*;
import static pt.isel.ls.command.strings.CommandEnum.DURATION;
import static pt.isel.ls.command.strings.CommandEnum.YEAR;

public class PostCinemaIDTheaterIDSessions implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException {
        /*return "INSERT INTO CINEMA_SESSIONS VALUES ("+
                cmdBuilder.getParameter(String.valueOf(DATE))+","+
                cmdBuilder.getParameter(String.valueOf(MOVIES_ID))+","+
                cmdBuilder.getParameter(String.valueOf(THEATERS_ID))+");";
                */
    }

    @Override //malfunction with parameters
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO CINEMA_SESSION VALUES (?, ?, ?)");
        stmt.setString(1, cmdBuilder.getParameter((String.valueOf(DATE))));
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(MOVIES_ID))));
        stmt.setString(3, cmdBuilder.getParameter((String.valueOf(THEATERS_ID))));
        stmt.execute();
        System.out.println("Posted session");
    }
}
