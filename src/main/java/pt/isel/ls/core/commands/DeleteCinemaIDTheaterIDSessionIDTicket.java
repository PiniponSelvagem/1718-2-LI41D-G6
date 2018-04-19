package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.DeleteView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.SESSION_ID;
import static pt.isel.ls.core.strings.CommandEnum.TICKET_ID;

public class DeleteCinemaIDTheaterIDSessionIDTicket extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException, CommandException {
        //TODO:
        /*
            DELETE /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets - removes a ticket set, given the following parameter that can occur multiple times (e.g. tkid=A3&tkid=B5`).
                tkid - ticket identifier composed of the row letter and its number on the row.
        */


        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM TICKET WHERE sid=? AND tk=?" +
                        " INNER JOIN "
        );

        int i = 0;
        while (cmdBuilder.getParameterSize(String.valueOf(TICKET_ID)) >= i) {
            System.out.println("index:"+i+" tkid:"+
                    cmdBuilder.getParameter(String.valueOf(TICKET_ID), i++)
            );
        }
        //stmt.setString(2, cmdBuilder.getParameter(String.valueOf(TICKET_ID), i));

        stmt.setString(1, cmdBuilder.getId(String.valueOf(SESSION_ID)));
        stmt.executeQuery();

        return new DeleteView();
    }
}
