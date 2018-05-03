package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.DeleteView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.SESSION_ID;
import static pt.isel.ls.core.strings.CommandEnum.THEATER_ID;
import static pt.isel.ls.core.strings.CommandEnum.TICKET_ID;

public class DeleteCinemaIDTheaterIDSessionIDTicket extends Command {

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
                "DELETE FROM TICKET FROM TICKET INNER JOIN CINEMA_SESSION ON TICKET.sid = CINEMA_SESSION.sid AND CINEMA_SESSION.sid = ? " +
                        "INNER JOIN THEATER ON THEATER.tid = CINEMA_SESSION.tid AND THEATER.tid = ? " +
                        "WHERE " + sql.toString()
        );
        stmt.setString(1, cmdBuilder.getId(SESSION_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(THEATER_ID.toString()));

        ResultSet rs = stmt.executeQuery();

        stmt = connection.prepareStatement("update SEATS SET SEATS.seats = SEATS.seats + 1 from SEATS inner join THEATER on THEATER.tid = Seats.tid " +
                                                             "inner join CINEMA_SESSION on CINEMA_SESSION.sid = SEATS.sid " +
                                                             "WHERE THEATER.tid = ? and CINEMA_SESSION.sid = ?"
        );
        stmt.setString(1, cmdBuilder.getId(THEATER_ID.toString()));
        stmt.setString(2, cmdBuilder.getId(SESSION_ID.toString()));
        stmt.executeUpdate();

        if(rs != null) return new DeleteView();
        
        else return new InfoNotFoundView();
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
