package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.*;
import static pt.isel.ls.command.strings.CommandEnum.CITY;

public class PostMovies implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException {
        /*return "INSERT INTO MOVIE VALUES ('"+
                cmdBuilder.getParameter(String.valueOf(TITLE))+"',"+
                cmdBuilder.getParameter(String.valueOf(YEAR))+","+
                cmdBuilder.getParameter(String.valueOf(DURATION))+");";
                */
    }

    @Override //Comando a trabalhar faz post e emite mensagem
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO MOVIE VALUES (?, ?, ?)");
        stmt.setString(1, cmdBuilder.getParameter((String.valueOf(TITLE))));
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(YEAR))));
        stmt.setString(3, cmdBuilder.getParameter((String.valueOf(DURATION))));
        stmt.execute();
        System.out.println("Posted movie");

    }
}
