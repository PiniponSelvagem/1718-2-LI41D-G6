package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsDateIDView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketIDView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.CINEMA_ID;
import static pt.isel.ls.command.strings.CommandEnum.THEATER_ID;

public class GetCinemaIDTheaterIDSessionIDTicketID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        //TODO:
        /*
            GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid} - returns the detailed information of the ticket, includes session information.
        */

        ResultSet rs = null; //= stmt.executeQuery();

        GetCinemaIDTheaterIDSessionIDTicketIDView ticketsView = new GetCinemaIDTheaterIDSessionIDTicketIDView(
                Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))),
                Integer.parseInt(cmdBuilder.getId(String.valueOf(THEATER_ID))),
                Integer.parseInt(cmdBuilder.getId(String.valueOf(THEATER_ID)))
        );

        //TODO: something like GetCinemaIDSessionsID

        return ticketsView;
    }
}
