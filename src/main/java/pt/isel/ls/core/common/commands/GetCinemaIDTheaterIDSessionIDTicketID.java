package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.*;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.*;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterIDSessionIDTicketID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+THEATER_ID_FULL
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+SESSION_ID_FULL
                +DIR_SEPARATOR+TICKETS+DIR_SEPARATOR+TICKET_ID_FULL;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws SQLException {
        String cinemaID  = cmdBuilder.getId(CINEMA_ID);
        String theaterID = cmdBuilder.getId(THEATER_ID);
        String sessionID = cmdBuilder.getId(SESSION_ID);
        String ticketID  = cmdBuilder.getId(TICKET_ID);
        DataContainer data=new DataContainer(this.getClass().getSimpleName());
        data.add(D_TICKET,  TicketsSQL.queryID(con, sessionID, ticketID));
        data.add(D_SESSION, SessionsSQL.queryID(con, sessionID));
        data.add(D_CINEMA,  CinemasSQL.queryID(con, cinemaID));
        data.add(D_THEATER, TheatersSQL.queryID(con, theaterID));

        Session session = (Session) data.getData(D_SESSION);
        if (session!=null)
            data.add(D_MOVIE, MoviesSQL.queryID(con, session.getMovieID()));

        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
