package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketIDView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.CINEMA_ID;
import static pt.isel.ls.command.strings.CommandEnum.THEATER_ID;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailable extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        //TODO:
        /*
            GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available - returns the number of available tickets for a session.
        */

        ResultSet rs = null; //= stmt.executeQuery();

        GetCinemaIDTheaterIDSessionIDTicketIDView ticketsView = new GetCinemaIDTheaterIDSessionIDTicketIDView(
                Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))),
                Integer.parseInt(cmdBuilder.getId(String.valueOf(THEATER_ID))),
                Integer.parseInt(cmdBuilder.getId(String.valueOf(THEATER_ID)))
        );

        while(rs.next()){
            //TODO: something like GetCinemaIDSessions
        }

        return ticketsView;
    }
}
