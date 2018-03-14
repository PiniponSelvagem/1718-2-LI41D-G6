package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.*;

import static pt.isel.ls.command.strings.CommandEnum.*;


public class GetCinemaIDSessionID implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        /*
        //TODO: TEST THIS!
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CINEMA_SESSIONS AS s INNER JOIN THEATER AS t " +
                "WHERE t.cid=? AND s.tid=t.tid AND s.sid=?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        stmt.setString(2, cmdBuilder.getId(String.valueOf(SESSION_ID)));
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();
        while(rs.next()){ //this while now iterates through every entry of the resultset as this command has to give all the info
            System.out.print("Session info: "+ rs.getString(1));
            for(int i =2; i<= metadata.getColumnCount();i++)
                System.out.print(", "+rs.getInt(i));
        }
        */
    }
}
