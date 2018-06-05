package pt.isel.ls.view.html;

import pt.isel.ls.core.common.commands.GetMovieIDTMDB;
import pt.isel.ls.core.common.commands.GetMovies;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Movie;
import pt.isel.ls.view.html.utils.HtmlViewCommon;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.common.headers.html.HtmlElem.submit;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;

public class GetMoviesTMDBView extends HtmlView {

    public GetMoviesTMDBView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        String[] tableColumns = {"Title", "Release Year"};
        Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);
        LinkedList<Movie> movies = (LinkedList<Movie>) data.getData(D_MOVIES);
        Writable[][] td = new Writable[movies.size()][tableColumns.length];
        Writable[] td_array = new Writable[movies.size()+1];
        td_array[0] = tr(th);

        String hyperLink = new GetMovieIDTMDB().getPath()
                .replace(TMDB_ID_FULL.toString(), "%s");

        Movie movie;
        for (int j = 0; j < movies.size(); j++) {
            movie = movies.get(j);
            td[j][0] = td(a(String.format(hyperLink, movie.getId()), movie.getTitle()));
            td[j][1] = td(text(Integer.toString(movie.getYear())));
            td_array[j + 1] = tr(td[j]);
        }

        return new HtmlPage("Add Movie",
                h3(a("/movies", "Movies")),
                h1(text("Which Movie you want to add? ")),
                table(td_array),
                breakLine(),
                form(POST.toString(), new GetMovies().getPath(),
                        text("Still not finding? Try adding one manually."), breakLine(),
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
