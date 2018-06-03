package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.common.commands.db_queries.TheatersSQL;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessions extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+SESSIONS;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws SQLException {
        String cinemaID = cmdBuilder.getId(CINEMA_ID);
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_SESSIONS, SessionsSQL.queryForCinema(con, cinemaID));
        data.add(D_MOVIES,   MoviesSQL.queryForCinema(con, cinemaID));
        data.add(D_THEATERS, TheatersSQL.queryForCinema(con, cinemaID));
        data.add(D_CID,      cinemaID);
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
