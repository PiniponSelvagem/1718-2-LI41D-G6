package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheatersIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetCinemaIDTheatersID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM THEATER AS t WHERE t.cid=? AND t.tid=?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(THEATER_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            return new InfoNotFoundView();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        data.add(new Theater(rs.getInt(1), rs.getString(5), rs.getInt(3), rs.getInt(4), rs.getInt(2),
                Integer.parseInt(cmdBuilder.getId(THEATER_ID.toString()))));

        return new GetCinemaIDTheatersIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
