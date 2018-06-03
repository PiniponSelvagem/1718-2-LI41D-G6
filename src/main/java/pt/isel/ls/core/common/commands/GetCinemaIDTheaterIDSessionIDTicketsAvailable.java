package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_AVAILABLE_SEATS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SID;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailable extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+THEATER_ID_FULL
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+SESSION_ID_FULL
                +DIR_SEPARATOR+TICKETS+DIR_SEPARATOR+AVAILABLE;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws SQLException {
        String sessionID = cmdBuilder.getId(SESSION_ID);
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_AVAILABLE_SEATS, SessionsSQL.queryAvailableSeats(con, sessionID));
        data.add(D_SID, sessionID);
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
