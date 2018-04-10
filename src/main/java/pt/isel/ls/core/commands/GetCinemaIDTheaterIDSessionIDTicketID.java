package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketIDView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID;
import static pt.isel.ls.core.strings.CommandEnum.THEATER_ID;

public class GetCinemaIDTheaterIDSessionIDTicketID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        //TODO:
        /*
            GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid} - returns the detailed information of the ticket, includes session information.
        */

        ResultSet rs = null; //= stmt.executeQuery();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());

        //TODO: something like GetCinemaIDSessionsID

        return new GetCinemaIDTheaterIDSessionIDTicketIDView(
                data,
                Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))),
                Integer.parseInt(cmdBuilder.getId(String.valueOf(THEATER_ID))),
                Integer.parseInt(cmdBuilder.getId(String.valueOf(THEATER_ID)))
        );
    }
}
