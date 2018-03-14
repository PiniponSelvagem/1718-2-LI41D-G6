package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class GetCinemaIDTheaters implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        /*
        //TODO: TEST THIS!
        PreparedStatement stmt = connection.prepareStatement("SELECT t.tid FROM THEATER AS t " +
                "WHERE t.cid=?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            System.out.println("Theater id: "+rs.getInt(1));
        }
        */
    }
}
