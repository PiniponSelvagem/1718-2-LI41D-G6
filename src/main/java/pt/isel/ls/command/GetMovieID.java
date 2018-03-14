package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.*;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class GetMovieID implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MOVIE WHERE mid = ?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(MOVIE_ID)));
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();
        while(rs.next()){ //this while now iterates through every entry of the resultset as this command has to give all the info
            System.out.print("Movie info: "+ rs.getString(1));
            for(int i =2; i<= metadata.getColumnCount();i++)
                System.out.print(", "+rs.getInt(i));
        }

    }
}
