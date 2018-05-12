package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheatersIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATER;

public class GetCinemaIDTheatersID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM THEATER AS t WHERE t.cid=? AND t.tid=?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(THEATER_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());

        if (!rs.next())
            return new InfoNotFoundView(data);

        data.add(D_THEATER, new Theater(rs.getInt(1), rs.getString(5), rs.getInt(3), rs.getInt(4), rs.getInt(2),
                Integer.parseInt(cmdBuilder.getId(THEATER_ID.toString()))));

        return new GetCinemaIDTheatersIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
