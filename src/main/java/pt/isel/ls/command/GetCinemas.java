package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemasView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetCinemas implements Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * from CINEMA");
        ResultSet rs = stmt.executeQuery();

        GetCinemasView cinemasView = new GetCinemasView();

        while(rs.next()){
            cinemasView.add(new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }

        return cinemasView;
    }
}
