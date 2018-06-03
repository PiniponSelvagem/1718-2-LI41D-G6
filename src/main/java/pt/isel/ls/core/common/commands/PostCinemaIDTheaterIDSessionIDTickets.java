package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.SQLData;
import pt.isel.ls.core.common.commands.db_queries.TicketsSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.TICKET_SEAT_INVALID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


public class PostCinemaIDTheaterIDSessionIDTickets extends Command {

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+THEATER_ID_FULL
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+SESSION_ID_FULL
                +DIR_SEPARATOR+TICKETS;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws ParameterException, SQLException {
        String sessionID = cmdBuilder.getId(SESSION_ID);

        int seat;
        try {
            seat = Integer.parseInt(cmdBuilder.getParameter(SEATS_ROW));
        } catch (NumberFormatException e) {
            throw new ParameterException(TICKET_SEAT_INVALID);
        }

        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        SQLData postData;
        postData = TicketsSQL.postTicket(con,
                sessionID,
                cmdBuilder.getParameter(ROWS),
                seat);
        data.add(D_SQL, postData);
        data.add(D_CID,  cmdBuilder.getId(CINEMA_ID));
        data.add(D_TID,  cmdBuilder.getId(THEATER_ID));
        data.add(D_SID,  sessionID);
        data.add(D_TKID, postData.getId());

        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
