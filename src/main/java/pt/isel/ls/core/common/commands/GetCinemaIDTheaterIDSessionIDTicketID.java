package pt.isel.ls.core.common.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.common.commands.db_queries.*;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.*;
import pt.isel.ls.sql.Sql;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.SQL_ERROR;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterIDSessionIDTicketID extends Command {
    private final static Logger log = LoggerFactory.getLogger(GetCinemaIDTheaterIDSessionIDTicketID.class);

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
    public DataContainer execute(CommandBuilder cmdBuilder) {
        String cinemaID  = cmdBuilder.getId(CINEMA_ID);
        String theaterID = cmdBuilder.getId(THEATER_ID);
        String sessionID = cmdBuilder.getId(SESSION_ID);
        String ticketID = cmdBuilder.getId(TICKET_ID);
        DataContainer data=new DataContainer(this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            data.add(D_TICKET,  TicketsSQL.queryID(con, sessionID, ticketID));
            data.add(D_SESSION, SessionsSQL.queryID(con, sessionID));
            data.add(D_CINEMA,  CinemasSQL.queryID(con, cinemaID));
            data.add(D_THEATER, TheatersSQL.queryID(con, theaterID));

            Session session = (Session) data.getData(D_SESSION);
            if (session!=null)
                data.add(D_MOVIE, MoviesSQL.queryID(con, session.getMovieID()));
            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e1) {
                    log.error(String.format(SQL_ERROR.toString(), e.getErrorCode(), e.getMessage()), this.hashCode());
                }
            }
            log.error(String.format(SQL_ERROR.toString(), e.getErrorCode(), e.getMessage()), this.hashCode());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.error(String.format(SQL_ERROR.toString(), e.getErrorCode(), e.getMessage()), this.hashCode());
                }
            }
        }

        return data;
    }
}
