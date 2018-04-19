package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketsAvailableView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID;
import static pt.isel.ls.core.strings.CommandEnum.SESSION_ID;
import static pt.isel.ls.core.strings.CommandEnum.THEATER_ID;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailable extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        //TODO:
        /*
            GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available - returns the number of available tickets for a session.
        */
        int availableSeats = 0;

        PreparedStatement stmt1 = connection.prepareStatement(
                "SELECT DISTINCT t.SeatsAvailable FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON s.tid=t.tid AND t.tid=? " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid AND c.cid=? " +
                "WHERE s.sid=?"
        );
        stmt1.setString(1, cmdBuilder.getId(String.valueOf(THEATER_ID)));
        stmt1.setString(2, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        stmt1.setString(3, cmdBuilder.getId(String.valueOf(SESSION_ID)));
        ResultSet rs = stmt1.executeQuery();
        if(rs.next()) availableSeats = rs.getInt(1);

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        data.add(availableSeats);

        return new GetCinemaIDTheaterIDSessionIDTicketsAvailableView(data,Integer.parseInt(cmdBuilder.getId(String.valueOf(SESSION_ID))));
    }
}
