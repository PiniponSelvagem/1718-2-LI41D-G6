package pt.isel.ls.view.command;

import pt.isel.ls.core.common.commands.GetMovieID;
import pt.isel.ls.core.common.commands.GetMovies;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.command.utils.HtmlViewCommon;
import static pt.isel.ls.core.common.headers.Html.*;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.tr;
import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.MOVIE_ID_FULL;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;


public class GetMoviesView extends CommandView {

    public GetMoviesView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Movies");
        String[] tableColumns = {"ID", "Title", "Year", "Duration"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        String[] tableColumns = {"Title", "Release Year", "Duration"};
        Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);
        LinkedList<Movie> movies = (LinkedList<Movie>) data.getData(D_MOVIES);
        Writable[][] td = new Writable[movies.size()][tableColumns.length];
        Writable[] td_array = new Writable[movies.size()+1];
        td_array[0] = tr(th);
        Movie movie;
        String hyperLink = new GetMovieID().getPath()
                .replace(MOVIE_ID_FULL.toString(), "%d");
        for (int i = 0; i < movies.size(); i++) {
            movie = movies.get(i);
            td[i][0] = td(a(String.format(hyperLink, movie.getId()), movie.getTitle()));
            td[i][1] = td(text(Integer.toString(movie.getYear())));
            td[i][2] = td(text(Integer.toString(movie.getDuration())));
            td_array[i+1] = tr(td[i]);
        }

        header = new HtmlPage("Movies",
                h3(a(DIR_SEPARATOR.toString(), "Main page")),
                h1(text("Movies: ")),
                table(td_array));

        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "title", "year", "duration"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Movie> movies = (LinkedList<Movie>) data.getData(D_MOVIES);
        String[][] tableData  = new String[movies.size()][columnNames.length];
        Movie movie;
        for (int y=0; y<movies.size(); ++y) {
            movie = movies.get(y);
            tableData[y][0] = String.valueOf(movie.getId());
            tableData[y][1] = String.valueOf(movie.getTitle());
            tableData[y][2] = String.valueOf(movie.getYear());
            tableData[y][3] = String.valueOf(movie.getDuration());
        }
        return tableData;
    }
}
