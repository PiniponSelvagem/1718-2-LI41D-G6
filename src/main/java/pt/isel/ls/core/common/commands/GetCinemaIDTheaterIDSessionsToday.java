package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterIDSessionsToday extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+THEATER_ID_FULL+DIR_SEPARATOR+SESSIONS
                +DIR_SEPARATOR+TODAY;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws SQLException {
        String cinemaID  = cmdBuilder.getId(CINEMA_ID);
        String theaterID = cmdBuilder.getId(THEATER_ID);
        Date date = new java.sql.Date(new java.util.Date().getTime());
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_SESSIONS, SessionsSQL.queryForTheaterAndDate(con, theaterID, date.toString()));
        data.add(D_MOVIES,   MoviesSQL.queryForTheaterAndDate(con, theaterID, date.toString()));
        data.add(D_CID,      cinemaID);
        data.add(D_TID,      theaterID);
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
