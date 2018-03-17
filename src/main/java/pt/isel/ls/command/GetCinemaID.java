package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDView;

import java.sql.*;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class GetCinemaID implements Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CINEMA WHERE cid = ?");
        stmt.setString(1, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            return null;

        return new GetCinemaIDView(new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));
    }
}
