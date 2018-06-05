package pt.isel.ls.core.utils.themoviedb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.exceptions.TheMoviesDBException;
import pt.isel.ls.model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static pt.isel.ls.core.strings.ExceptionEnum.TMDB_EXCEPTION;

public class MoviesDB extends TheMovieDB {
    private final static Logger log = LoggerFactory.getLogger(MovieDB.class);

    private String pathSearch;

    public MoviesDB(String searchFor) {
        searchFor = searchFor.replace(" ", SPACE_REPLACE);
        pathSearch = URL+SEARCH+API_KEY+QUERY+searchFor+PAGE_NUMBER;
    }

    public List<Movie> getMovies() throws TheMoviesDBException {
        LinkedList<Movie> movies = new LinkedList<>();

        try {
            //Do query to TheMoviesDB, and get JsonObject
            InputStream stream = new URL(pathSearch).openStream();
            String body = inputStreamToString(stream);
            JSONObject json = new JSONObject(body);

            JSONArray moviesArray = json.getJSONArray(RESULTS);

            JSONObject movie;
            for (int i=0; i<moviesArray.length(); ++i) {
                movie = (JSONObject) moviesArray.get(i);
                if (movie != null)
                    movies.add(
                            new Movie(
                                    movie.getString(ID),
                                    parseTitle(movie),
                                    parseYear(movie),
                                    parseDuration(movie)
                            )
                    );
            }

        } catch (JSONException | IOException e) {
            log.error(e.getMessage(), this.hashCode());
            throw new TheMoviesDBException(TMDB_EXCEPTION, e.getMessage());
        }

        return movies;
    }
}
