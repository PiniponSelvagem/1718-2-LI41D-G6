package pt.isel.ls.core.common.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.common.commands.db_queries.CinemasSQL;
import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.common.commands.db_queries.TheatersSQL;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.sql.Sql;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.SQL_ERROR;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessionsToday extends Command {
    private final static Logger log = LoggerFactory.getLogger(GetCinemaIDSessionsToday.class);

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL+DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+TODAY;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder) {
        String cinemaID = cmdBuilder.getId(CINEMA_ID);
        Date date = new java.sql.Date(new java.util.Date().getTime());
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            data.add(D_CINEMA, CinemasSQL.queryID(con, cinemaID));
            data.add(D_SESSIONS, SessionsSQL.queryForCinemaAndDate(con, cinemaID, date.toString()));
            data.add(D_THEATERS, TheatersSQL.queryForCinema(con, cinemaID));
            data.add(D_MOVIES,   MoviesSQL.queryForCinema(con, cinemaID));
            data.add(D_CID,  cinemaID);
            data.add(D_DATE, date);
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

