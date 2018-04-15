package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketsAvailableView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.SESSION_ID;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailable extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        //TODO:
        /*
            GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available - returns the number of available tickets for a session.
        */

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT DISTINCT t.SeatsAvailable FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON s.tid=t.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                "WHERE s.sid=?"
        );
        stmt.setString(1, cmdBuilder.getId(String.valueOf(SESSION_ID)));
        ResultSet rs = stmt.executeQuery();
        int availableSeats = rs.getInt(1);

        stmt = connection.prepareStatement(
                "SELECT DISTINCT COUNT(tk.tkid) FROM TICKET AS tk " +
                "INNER JOIN CINEMA_SESSION AS s ON tk.sid=s.sid " +
                "INNER JOIN THEATER AS t ON s.tid=t.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                "INNER JOIN MOVIE AS t ON m.mid=s.mid "+
                "WHERE s.sid=?"
        );
        stmt.setString(1, cmdBuilder.getId(String.valueOf(SESSION_ID)));
        rs = stmt.executeQuery();
        availableSeats -= rs.getInt(1);

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        data.add(availableSeats);

        return new GetCinemaIDTheaterIDSessionIDTicketsAvailableView(data,Integer.parseInt(cmdBuilder.getId(String.valueOf(SESSION_ID))));
    }
}
