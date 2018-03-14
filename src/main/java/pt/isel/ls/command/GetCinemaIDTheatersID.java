package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.*;

public class GetCinemaIDTheatersID implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        //"SELECT * FROM THEATER AS t WHERE t.cid="+ cmdBuilder.popId()+" AND t.tid="+cmdBuilder.popId();
    }

    @Override //not tested, impossible to do so without working post command
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM THEATER AS t " +
                "WHERE t.cid=? AND t.tid=?");
        stmt.setInt(1, cmdBuilder.popId());
        stmt.setInt(2, cmdBuilder.popId());
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();
        while(rs.next()){ //this while now iterates through every entry of the resultset as this command has to give all the info
            System.out.print("Theater info: "+ rs.getString(1));
            for(int i =2; i<= metadata.getColumnCount();i++)
                System.out.print(", "+rs.getInt(i));
        }
    }
}
