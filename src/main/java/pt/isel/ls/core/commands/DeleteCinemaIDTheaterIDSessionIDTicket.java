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
        /*
            DELETE /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets - removes a ticket set, given the following parameter that can occur multiple times (e.g. tkid=A3&tkid=B5`).
                tkid - ticket identifier composed of the row letter and its number on the row.
        */

        StringBuilder sql = new StringBuilder();
        int paramSize = cmdBuilder.getParamContainerSize();

        int i = 0;
        while (i < paramSize) {
            sql.append("tkid=").append("'").append(cmdBuilder.getParameter(String.valueOf(TICKET_ID), i++)).append("'");
            if(i < paramSize) sql.append(" OR ");
        }

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM TICKET FROM TICKET INNER JOIN CINEMA_SESSION ON TICKET.sid = CINEMA_SESSION.sid AND CINEMA_SESSION.sid = ? " +
                        "WHERE " + sql.toString()
        );

        stmt.setString(1, cmdBuilder.getId(String.valueOf(SESSION_ID)));
        stmt.executeUpdate();

        return new DeleteView();
    }
}
