package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.DeleteView;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteCinemaIDTheaterIDSessionIDTicket extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        //TODO:
        /*
            DELETE /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets - removes a ticket set, given the following parameter that can occur multiple times (e.g. tkid=A3&tkid=B5`).
                tkid - ticket identifier composed of the row letter and its number on the row.
        */

        return new DeleteView();
    }
}
