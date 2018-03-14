package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetCinemaIDSessions implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        /*return "SELECT s.sid FROM CINEMA_SESSION AS s INNER JOIN THEATER AS t WHERE t.cid="+
                cmdBuilder.popId()+" AND t.tid=s.tid";*/
    }

    @Override //not tested, impossible to do so without working post command
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT s.sid FROM CINEMA_SESSION AS s INNER JOIN THEATER AS t" +
                " WHERE t.cid=? AND t.tid=s.tid");
        stmt.setInt(1, cmdBuilder.popId());
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            System.out.println("Session id: "+rs.getInt(1));
        }
    }
}
