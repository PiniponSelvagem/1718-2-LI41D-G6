package pt.isel.ls.core.utils.themoviedb;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class TheMovieDB {
    private final static Logger log = LoggerFactory.getLogger(MovieDB.class);

    static final String API_KEY = "api_key=9791eeaaecb0d0c3039376652da34130",   //PiniponSelvagem
            URL = "https://api.themoviedb.org/3/",                              //API base URL
            SPACE_REPLACE = "%20",
            SEARCH = "search/movie?",
            DETAILED = "movie/%s?",
            QUERY = "&query=",
            PAGE_NUMBER = "&page=1";

    static final String RESULTS = "results",
            ID = "id", TITLE = "title", RELEASE_DATE = "release_date", DURATION = "runtime",
            INVALID_AND = "&", REPLACE_AND = "and";


    static int parseYear(JSONObject jObj) {
        try {
            //Format date to only get year
            SimpleDateFormat sDF = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sDF.parse(jObj.get(RELEASE_DATE).toString());
            SimpleDateFormat yearDF = new SimpleDateFormat("yyyy");
            return Integer.parseInt(yearDF.format(date));
        } catch (Exception e) {
            return 0;
        }
    }

    static String parseTitle(JSONObject jObj) {
        try {
           return jObj.getString(TITLE).replace(INVALID_AND, REPLACE_AND);
        } catch (Exception e) {
            return null;
        }
    }

    static int parseDuration(JSONObject jObj) {
        try {
            return jObj.getInt(DURATION);
        } catch (Exception e) {
            return 0;
        }
    }

    // convert InputStream to String
    static String inputStreamToString(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        return sb.toString();
    }
}
