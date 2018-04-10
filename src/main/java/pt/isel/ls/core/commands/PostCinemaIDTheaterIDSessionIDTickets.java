package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.Connection;
import java.sql.SQLException;

public class PostCinemaIDTheaterIDSessionIDTickets extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {

        int id = -1;
        //TODO:
        /*
        POST /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets - creates a new ticket given the following parameters
            row - row letter;
            seat - seat number.
         */

        return new PostView("Ticket ID: ", id);
    }
}
