package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.DeleteView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                //"DELETE FROM TICKET WHERE TICKET.sid=? AND ("+sql.toString()+")"


                "DELETE FROM TICKET FROM TICKET INNER JOIN CINEMA_SESSION ON TICKET.sid = CINEMA_SESSION.sid AND CINEMA_SESSION.sid = ? " +
                        "INNER JOIN THEATER ON THEATER.tid = CINEMA_SESSION.tid AND THEATER.tid = ? " +
                        "WHERE " + sql.toString()

        );
        stmt.setString(1, cmdBuilder.getId(SESSION_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(THEATER_ID.toString()));

        int ret = stmt.executeUpdate();

        for (int x=0; x<i; ++x) {
            stmt = connection.prepareStatement("update SEATS SET SEATS.seats = SEATS.seats + 1 from SEATS inner join THEATER on THEATER.tid = Seats.tid " +
                    "inner join CINEMA_SESSION on CINEMA_SESSION.sid = SEATS.sid " +
                    "WHERE THEATER.tid = ? and CINEMA_SESSION.sid = ?"
            );
            stmt.setString(1, cmdBuilder.getId(THEATER_ID.toString()));
            stmt.setString(2, cmdBuilder.getId(SESSION_ID.toString()));
            stmt.executeUpdate();
        }

        if(ret != 0) return new DeleteView();

        return new InfoNotFoundView();

        //else return new InfoNotFoundView();
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
