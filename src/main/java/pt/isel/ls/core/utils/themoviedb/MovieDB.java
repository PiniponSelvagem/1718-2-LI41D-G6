package pt.isel.ls.core.utils.themoviedb;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.exceptions.TheMoviesDBException;
import pt.isel.ls.model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static pt.isel.ls.core.strings.ExceptionEnum.TMDB_EXCEPTION;

public class MovieDB extends TheMovieDB {
    private final static Logger log = LoggerFactory.getLogger(MovieDB.class);

    private String pathDetailed;
    private final String id;

    public MovieDB(String id) {
        this.id = id;
        pathDetailed = URL+String.format(DETAILED, id)+API_KEY;
    }

    public Movie getMovie() throws TheMoviesDBException {
        int year, duration;
        String title;

        try {
            //Get detailed information of the first movie that is found
            InputStream stream = new URL(String.format(pathDetailed, id)).openStream();
            String body = inputStreamToString(stream);
            JSONObject movieDetailed = new JSONObject(body);

            try {
                //Format date to only get year
                SimpleDateFormat sDF = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sDF.parse(movieDetailed.get(RELEASE_DATE).toString());
                SimpleDateFormat yearDF = new SimpleDateFormat("yyyy");
                year = Integer.parseInt(yearDF.format(date));
            } catch (Exception e) {
                year = 0;
            }

            try {
                title = movieDetailed.getString(TITLE).replace(INVALID_AND, REPLACE_AND);
            } catch (Exception e) {
                return null;    //if cant get the title, return null because movie isnt valid.
            }

            try {
                duration = movieDetailed.getInt(DURATION);
            } catch (Exception e) { duration = 0; }

            return new Movie(id, title, year, duration);

        } catch (JSONException | IOException e) {
            log.error(e.getMessage(), this.hashCode());
            throw new TheMoviesDBException(TMDB_EXCEPTION, e.getMessage());
        }
    }
}
