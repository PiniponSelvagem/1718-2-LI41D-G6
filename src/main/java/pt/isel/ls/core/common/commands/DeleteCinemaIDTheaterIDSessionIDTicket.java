package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.DeleteView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class DeleteCinemaIDTheaterIDSessionIDTicket extends Command {

    @Override
    public String getMethodName() {
        return DELETE.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX
                +DIR_SEPARATOR+TICKETS;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException, CommandException {

        StringBuilder sql = new StringBuilder();
        int paramSize = cmdBuilder.getParameterSize(TICKET_ID.toString());

        int i = 0;
        while (i < paramSize) {
            sql.append("tkid=").append("'").append(cmdBuilder.getParameter(TICKET_ID.toString(), i++)).append("'");
            if(i < paramSize) sql.append(" OR ");
        }

        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM TICKET "+
                        "WHERE TICKET.sid=? AND (" + sql.toString()+")"
        );
        stmt.setString(1, cmdBuilder.getId(SESSION_ID.toString()));

        int test=stmt.executeUpdate();
        while(paramSize>0) {
            stmt = connection.prepareStatement("UPDATE CINEMA_SESSION SET CINEMA_SESSION.SeatsAvailable = CINEMA_SESSION.SeatsAvailable + 1 " +
                    "WHERE CINEMA_SESSION.sid = ?"
            );
            stmt.setString(1, cmdBuilder.getId(SESSION_ID.toString()));
            stmt.executeUpdate();
            paramSize--;
        }
        if(test != 0) return new DeleteView();

        else return new InfoNotFoundView(new DataContainer(cmdBuilder.getHeader()));
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
