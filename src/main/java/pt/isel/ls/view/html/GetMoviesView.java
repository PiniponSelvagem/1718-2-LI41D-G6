package pt.isel.ls.view.html;

import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.common.commands.GetMovieID;
import pt.isel.ls.core.common.commands.GetMovies;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.html.utils.HtmlViewCommon;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.common.headers.html.HtmlElem.submit;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;


public class GetMoviesView extends HtmlView {

    public GetMoviesView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        String[] tableColumns = {"Title", "Release Year", "Duration"};
        Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);
        LinkedList<Movie> movies = (LinkedList<Movie>) data.getData(D_MOVIES);
        Writable[][] td = new Writable[movies.size()][tableColumns.length];
        Writable[] td_array = new Writable[movies.size()+1];
        td_array[0] = tr(th);
        String hyperLink = new GetMovieID().getPath()
                .replace(MOVIE_ID_FULL.toString(), "%s");

        HtmlViewCommon.fillTableDataMovies(movies, td, td_array, hyperLink);

        return new HtmlPage("Movies",
                h3(a(DIR_SEPARATOR.toString(), "Main page")),
                h1(text("Movies: ")),
                table(td_array),
                breakLine(),
                form(POST.toString(), new GetMovies().getPath(),
                        text("Title: "), breakLine(),
                        textInput(TITLE.toString()), breakLine(),
                        text("Duration:"), breakLine(),
                        textInput(DURATION.toString()), breakLine(),
                        text("Release year:"), breakLine(),
                        textInput(YEAR.toString()), breakLine(),
                        submit("Create")
                )
        );
    }

    @Override
    public HttpStatusCode getCode() {
        return HttpStatusCode.OK;
    }
}
