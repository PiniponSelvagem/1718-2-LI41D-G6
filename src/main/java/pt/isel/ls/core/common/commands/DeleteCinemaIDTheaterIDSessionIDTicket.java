package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.TicketsSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_DELETE;

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
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws ParameterException, SQLException {
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_DELETE,
                TicketsSQL.deleteTicket(con,
                    cmdBuilder.getId(SESSION_ID),
                    cmdBuilder.getParametersList(TICKET_ID),
                    cmdBuilder.getParameterSize(TICKET_ID)
                )
        );
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
