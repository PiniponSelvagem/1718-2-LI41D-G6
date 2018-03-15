package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetCinemas implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("SELECT cid from CINEMA");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            System.out.println("Cinema id: "+rs.getInt(1));
        }
    }
}
