package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.CinemasSQL;
import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetMovieID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+MOVIE_ID_FULL;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws SQLException {
        String movieID = cmdBuilder.getId(MOVIE_ID);
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_MOVIE,    MoviesSQL.queryID(con, movieID));
        data.add(D_CINEMAS,  CinemasSQL.queryPlayingMovieID(con, movieID));
        data.add(D_SESSIONS, SessionsSQL.queryPlayingMovieID(con, movieID));
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
