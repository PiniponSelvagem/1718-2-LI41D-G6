package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.exceptions.TheMoviesDBException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.themoviedb.MoviesDB;
import pt.isel.ls.model.Movie;

import java.sql.Connection;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;

public class GetMoviesTMDB extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+TMDB;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws ParameterException, TheMoviesDBException {
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        LinkedList<Movie> movies = (LinkedList<Movie>) new MoviesDB(cmdBuilder.getParameter(TITLE)).getMovies();
        data.add(D_MOVIES, movies);
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return false;
    }
}
