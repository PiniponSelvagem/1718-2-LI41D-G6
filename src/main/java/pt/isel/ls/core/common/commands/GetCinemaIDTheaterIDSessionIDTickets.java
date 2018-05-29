package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.common.commands.db_queries.TheatersSQL;
import pt.isel.ls.core.common.commands.db_queries.TicketsSQL;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;
import pt.isel.ls.sql.Sql;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterIDSessionIDTickets extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+THEATER_ID_FULL
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+SESSION_ID_FULL
                +DIR_SEPARATOR+TICKETS;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder) {
        int cinemaID  = Integer.parseInt(cmdBuilder.getId(CINEMA_ID));
        int theaterID = Integer.parseInt(cmdBuilder.getId(THEATER_ID));
        int sessionID = Integer.parseInt(cmdBuilder.getId(SESSION_ID));
        DataContainer data=new DataContainer(this.getClass().getSimpleName(), cmdBuilder.getHeader());
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            data.add(D_TICKETS, new LinkedList<>(TicketsSQL.queryAll(con, sessionID).values()));
            data.add(D_THEATER, TheatersSQL.queryID(con, theaterID));

            Session session = SessionsSQL.queryID(con, sessionID);
            data.add(D_SESSION, session);
            if (session != null) {
                data.add(D_MOVIE, MoviesSQL.queryID(con, session.getMovieID()));
            }

            data.add(D_CID, cinemaID);
            data.add(D_SID, sessionID);
            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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

        return data;
    }
}
