package pt.isel.ls.core.utils.themoviedb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pt.isel.ls.core.exceptions.TheMoviesDBException;
import pt.isel.ls.model.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static pt.isel.ls.core.strings.ExceptionEnum.TMDB_EXCEPTION;

public class MovieDB {

    private static final String API_KEY = "api_key=9791eeaaecb0d0c3039376652da34130",   //PiniponSelvagem
                                URL = "https://api.themoviedb.org/3/",                  //API base URL
                                SPACE_REPLACE = "%20",
                                SEARCH = "search/movie?",
                                DETAILED = "movie/%s?",
                                QUERY = "&query=",
                                PAGE_NUMBER = "&page=1";

    private static final String RESULTS = "results",
                                ID = "id", TITLE = "title", RELEASE_DATE = "release_date", DURATION = "runtime",
                                INVALID_AND = "&", REPLACE_AND = "and";

    private static final int ANTI_COOLDOWN = 8;

    private String pathSearch;
    private String pathDetailed;

    public MovieDB(String searchFor) {
        searchFor = searchFor.replace(" ", SPACE_REPLACE);
        pathSearch = URL+SEARCH+API_KEY+QUERY+searchFor+PAGE_NUMBER;
        pathDetailed = URL+DETAILED+API_KEY;
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
            for (int i=0; i<moviesArray.length() && i<=ANTI_COOLDOWN; ++i) {
                movie = (JSONObject) moviesArray.get(i);
                if (movie != null)
                    movies.add(getMovie(String.valueOf(movie.get(ID))));
            }

        } catch (JSONException | IOException e) {
            throw new TheMoviesDBException(TMDB_EXCEPTION, e.getMessage());
        }

        return movies;
    }

    private Movie getMovie(String id) throws TheMoviesDBException {
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
            } catch (Exception e) { year = 0; }

            try {
                title = movieDetailed.get(TITLE).toString().replace(INVALID_AND, REPLACE_AND);
            } catch (Exception e) {
                return null;    //if cant get the title, return null because movie isnt valid.
            }

            try {
                duration = (int) movieDetailed.get(DURATION);
            } catch (Exception e) { duration = 0; }

            return new Movie(id, title, year, duration);

        } catch (JSONException | IOException e) {
            throw new TheMoviesDBException(TMDB_EXCEPTION, e.getMessage());
        }
    }

    // convert InputStream to String
    private static String inputStreamToString(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
