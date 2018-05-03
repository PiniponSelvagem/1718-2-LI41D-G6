package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetCinemaID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CINEMA WHERE cid = ?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            return new InfoNotFoundView();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        data.add(new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));

        return new GetCinemaIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
