package pt.isel.ls.view.html.utils;

import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Movie;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.html.Html.*;

public class HtmlViewCommon {

    /**
     * Fills the html header with column names
     * @param columns Columns names
     * @return Returns th header to use for the table
     */
    public static Writable[] fillTableHeader(String[] columns) {
        Writable[] th = new Writable[columns.length];
        for (int i=0; i<columns.length; ++i) {
            th[i] = th(text(columns[i]));
        }
        return th;
    }


    /**
     * Fills TableData with movies.
     * @param movies Movies list
     * @param td TableData values of one movie
     * @param td_array TableData movies
     * @param hyperLink link to the movie
     */
    public static void fillTableDataMovies(LinkedList<Movie> movies, Writable[][] td, Writable[] td_array, String hyperLink) {
        Movie movie;
        for (int j = 0; j < movies.size(); j++) {
            movie = movies.get(j);
            td[j][0] = td(a(String.format(hyperLink, movie.getId()), movie.getTitle()));
            td[j][1] = td(text(Integer.toString(movie.getYear())));
            td[j][2] = td(text(Integer.toString(movie.getDuration())));
            td_array[j + 1] = tr(td[j]);
        }
    }
}
