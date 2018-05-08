package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketsAvailableView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailable extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX
                +DIR_SEPARATOR+TICKETS+DIR_SEPARATOR+AVAILABLE;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        int availableSeats;
        String sid =cmdBuilder.getId(SESSION_ID.toString());

        PreparedStatement stmt = connection.prepareStatement("SELECT CINEMA_SESSION.SeatsAvailable from CINEMA_SESSION where CINEMA_SESSION.sid=?");
        stmt.setString(1, sid);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            availableSeats = rs.getInt(1);
            DataContainer data = new DataContainer(cmdBuilder.getHeader());
            data.add(availableSeats);
            return new GetCinemaIDTheaterIDSessionIDTicketsAvailableView(data,Integer.parseInt(sid));
        }
        return new InfoNotFoundView();
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
