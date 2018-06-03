package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.CinemasSQL;
import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.TheatersSQL;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMA;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATERS;

public class GetCinemaID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws SQLException {
        String cinemaID = cmdBuilder.getId(CINEMA_ID);
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_CINEMA,   CinemasSQL.queryID(con, cinemaID));
        data.add(D_THEATERS, new LinkedList<>(TheatersSQL.queryForCinema(con, cinemaID).values()));
        data.add(D_MOVIES,   new LinkedList<>(MoviesSQL.queryForCinema(con, cinemaID).values()));
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}