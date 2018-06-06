package pt.isel.ls.view.html;

import pt.isel.ls.core.common.commands.GetMovies;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.common.headers.html.HtmlElem.submit;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIE;

public class GetMovieIDTMDBView extends HtmlView {

    public GetMovieIDTMDBView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        Movie movie = (Movie) data.getData(D_MOVIE);

        return new HtmlPage("Add Movie",
                h3(a("/movies", "Movies")),
                h1(text("Creating movie")),
                h3(text("Is this information correct?")),
                li(text("Title: " + movie.getTitle())),
                li(text("Release year: " + movie.getYear())),
                li(text("Duration: " + movie.getDuration() + " minutes")),
                breakLine(),
                form(
                        POST.toString(), new GetMovies().getPath(),
                        hiddenInput("title", movie.getTitle()),
                        hiddenInput("duration", String.valueOf(movie.getDuration())),
                        hiddenInput("releaseYear", String.valueOf(movie.getYear())),
                        submit("Create")
                )
        );
    }

    @Override
    public HttpStatusCode getCode() {
        return HttpStatusCode.OK;
    }
}
