package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GetCinemaIDTheaterIDSessions implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        /*return "SELECT s.sid FROM CINEMA_SESSION AS s INNER JOIN THEATER AS t WHERE t.cid="+
                cmdBuilder.popId()+" AND s.tid=t.tid AND t.tid="+cmdBuilder.popId();*/
    }

    @Override //not tested, impossible to do so without working post command
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT s.sid FROM CINEMA_SESSION AS s INNER JOIN THEATER AS t " +
                "WHERE t.cid=? AND s.tid=t.tid AND t.tid=?");
        stmt.setInt(1, cmdBuilder.popId());
        stmt.setInt(2, cmdBuilder.popId());
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            System.out.println("Session id: "+rs.getInt(1));
        }
    }
}
