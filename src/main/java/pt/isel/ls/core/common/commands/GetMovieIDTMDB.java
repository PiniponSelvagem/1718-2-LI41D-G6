package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.TheMoviesDBException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.themoviedb.MovieDB;
import pt.isel.ls.model.Movie;

import java.sql.Connection;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIE;

public class GetMovieIDTMDB extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+TMDB+DIR_SEPARATOR+TMDB_ID_FULL;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws TheMoviesDBException {
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        Movie movie = new MovieDB(cmdBuilder.getId(TMDB_ID)).getMovie();
        data.add(D_MOVIE, movie);
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return false;
    }
}
