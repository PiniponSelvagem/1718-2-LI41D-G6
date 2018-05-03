package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketsAvailableView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.SESSION_ID;
import static pt.isel.ls.core.strings.CommandEnum.THEATER_ID;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailable extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        int availableSeats;
        String sid =cmdBuilder.getId(SESSION_ID.toString());
        String tid = cmdBuilder.getId(THEATER_ID.toString());

        PreparedStatement stmt = connection.prepareStatement("SELECT SEATS.seats as SeatsAvailable from SEATS where SEATS.sid=? AND SEATS.tid=?");
        stmt.setString(1, sid);
        stmt.setString(2, tid);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            availableSeats = rs.getInt(1);
            DataContainer data = new DataContainer(cmdBuilder.getHeader());
            data.add(availableSeats);
            return new GetCinemaIDTheaterIDSessionIDTicketsAvailableView(data,Integer.parseInt(cmdBuilder.getId(SESSION_ID.toString())));
        }

        return new InfoNotFoundView();
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
