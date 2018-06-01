package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.commands.db_queries.TicketsSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.sql.Sql;

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
    public DataContainer execute(CommandBuilder cmdBuilder) throws ParameterException {
        String sessionID = cmdBuilder.getId(SESSION_ID);

        int seat;
        try {
            seat = Integer.parseInt(cmdBuilder.getParameter(SEATS_ROW));
        } catch (NumberFormatException e) {
            throw new ParameterException(TICKET_SEAT_INVALID);
        }

        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        Connection con = null;
        PostData postData = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            postData = TicketsSQL.postTicket(con,
                                    sessionID,
                                    cmdBuilder.getParameter(ROWS),
                                    seat);
            data.add(D_POST, postData);
            con.commit();
        } catch (SQLException e) {
            data.add(D_POST, new PostData<>(e.getErrorCode(), e.getMessage()));
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            //TODO: catch excp handling
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        data.add(D_CID, Integer.parseInt(cmdBuilder.getId(CINEMA_ID)));
        data.add(D_TID, Integer.parseInt(cmdBuilder.getId(THEATER_ID)));
        data.add(D_SID, sessionID);
        if (postData != null) {
            data.add(D_TKID,postData.getId());
        }

        return data;
    }
}
