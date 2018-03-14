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

public class GetCinemaIDSessionsToday implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
    }

    @Override //not tested, impossible to do so without working post command
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date= dtf.format(localDate);

        PreparedStatement stmt = connection.prepareStatement("SELECT s.sid FROM CINEMA_SESSION AS s INNER JOIN THEATER AS t " +
                "WHERE t.cid=? AND t.tid=s.tid AND (SELECT CONVERT (s.Date, GETDATE()))=?");
        stmt.setInt(1, cmdBuilder.popId());
        stmt.setString(2, date);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            System.out.println("Session id: "+rs.getInt(1));
        }
    }
}
