package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketsAvailableView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_AVAILABLE_SEATS;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailable extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+THEATER_ID_FULL
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+SESSION_ID_FULL
                +DIR_SEPARATOR+TICKETS+DIR_SEPARATOR+AVAILABLE;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        int availableSeats;
        String sid = cmdBuilder.getId(SESSION_ID.toString());

        PreparedStatement stmt = connection.prepareStatement("SELECT CINEMA_SESSION.SeatsAvailable from CINEMA_SESSION where CINEMA_SESSION.sid=?");
        stmt.setString(1, sid);
        ResultSet rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        if(rs.next()){
            availableSeats = rs.getInt(1);
            data.add(D_AVAILABLE_SEATS, availableSeats);
            return new GetCinemaIDTheaterIDSessionIDTicketsAvailableView(data, Integer.parseInt(sid));
        }
        return new InfoNotFoundView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
