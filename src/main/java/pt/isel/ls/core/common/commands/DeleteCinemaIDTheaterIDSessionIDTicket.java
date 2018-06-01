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
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_DELETE;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_POST;

public class DeleteCinemaIDTheaterIDSessionIDTicket extends Command {

    @Override
    public String getMethodName() {
        return DELETE.toString();
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
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            data.add(D_DELETE,
                    TicketsSQL.deleteTicket(con,
                        cmdBuilder.getId(SESSION_ID),
                        cmdBuilder.getParametersList(TICKET_ID),
                        cmdBuilder.getParameterSize(TICKET_ID)
                    )
            );
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
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}
