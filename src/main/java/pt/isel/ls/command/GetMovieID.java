package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.*;

public class GetMovieID implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        //return "SELECT * FROM MOVIE WHERE mid="+cmdBuilder.popId();
    }

    @Override //Comando a funcionar
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MOVIE WHERE mid = ?");
        stmt.setInt(1, cmdBuilder.popId());
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();
        while(rs.next()){ //this while now iterates through every entry of the resultset as this command has to give all the info
            System.out.print("Movie info: "+ rs.getString(1));
            for(int i =2; i<= metadata.getColumnCount();i++)
                System.out.print(", "+rs.getInt(i));
        }

    }
}
