package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.*;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class GetCinemaIDTheatersID implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        /*
        //TODO: TEST THIS!
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM THEATER AS t " +
                "WHERE t.cid=? AND t.tid=?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        stmt.setString(2, cmdBuilder.getId(String.valueOf(THEATER_ID)));
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();
        while(rs.next()){ //this while now iterates through every entry of the resultset as this command has to give all the info
            System.out.print("Theater info: "+ rs.getString(1));
            for(int i =2; i<= metadata.getColumnCount();i++)
                System.out.print(", "+rs.getInt(i));
        }
        */
    }
}
