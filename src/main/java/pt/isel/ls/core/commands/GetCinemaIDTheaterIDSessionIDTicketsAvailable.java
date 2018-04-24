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
        String sid =cmdBuilder.getId(String.valueOf(SESSION_ID));
        String tid = cmdBuilder.getId(String.valueOf(THEATER_ID));

        PreparedStatement stmt = connection.prepareStatement("SELECT SEATS.seats as SeatsAvailable from SEATS where SEATS.sid=? AND SEATS.tid=?");
        stmt.setString(1, sid);
        stmt.setString(2, tid);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            availableSeats = rs.getInt(1);
            DataContainer data = new DataContainer(cmdBuilder.getHeader());
            data.add(availableSeats);
            return new GetCinemaIDTheaterIDSessionIDTicketsAvailableView(data,Integer.parseInt(cmdBuilder.getId(String.valueOf(SESSION_ID))));
        }

        return new InfoNotFoundView();

        /*PreparedStatement stmt1 = connection.prepareStatement(
                "SELECT DISTINCT t.SeatsAvailable FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON s.tid=t.tid " +
                "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                "WHERE s.sid=?"// AND c.cid=? AND t.tid=?
        );
        stmt1.setString(1,aux );
        ResultSet rs = stmt1.executeQuery();
        if(rs.next()) availableSeats = rs.getInt(1);

        stmt1 = connection.prepareStatement(
                "SELECT DISTINCT COUNT(tk.tkid) FROM TICKET AS tk " +
                        "WHERE tk.sid=?"// AND c.cid=? AND t.tid=?
        );
        stmt1.setString(1, aux);
        rs = stmt1.executeQuery();
        if(rs.next()) availableSeats = availableSeats-rs.getInt(1);*/
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
