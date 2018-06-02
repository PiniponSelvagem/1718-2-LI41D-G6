package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.exceptions.TheMoviesDBException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.themoviedb.MovieDB;
import pt.isel.ls.model.Movie;

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
        return ""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+THE_MOVIES_DB;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder) throws ParameterException, TheMoviesDBException {
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        LinkedList<Movie> movies = (LinkedList<Movie>) new MovieDB(cmdBuilder.getParameter(TITLE)).getMovies();
        data.add(D_MOVIES, movies);
        return data;
    }
}
