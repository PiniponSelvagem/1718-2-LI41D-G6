package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetMovies implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT mid from MOVIE");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            System.out.println("Movie id: "+rs.getInt(1));
        }

    }
}
