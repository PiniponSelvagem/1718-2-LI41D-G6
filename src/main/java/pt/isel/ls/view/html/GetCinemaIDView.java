package pt.isel.ls.view.html;

import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.common.commands.*;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.html.utils.HtmlViewCommon;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.common.headers.html.HtmlElem.submit;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDView extends HtmlView {

    public GetCinemaIDView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
        LinkedList<Theater> theaters = (LinkedList<Theater>) data.getData(D_THEATERS);
        LinkedList<Movie> movies = (LinkedList<Movie>) data.getData(D_MOVIES);

        if (cinema!=null) {
            String[] tableColumns = {"Name"};
            Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);

            Writable[][] td;
            Writable[] td_array = new Writable[theaters.size() + 1];
            td_array[0] = tr(th);
            Writable[] li_array = new Writable[theaters.size()];
            Theater theater;
            String hyperLink = new GetCinemaIDTheaterID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%s")
                    .replace(THEATER_ID_FULL.toString(), "%s"); //get path and make it ready to add IDs
            for (int y = 0; y < theaters.size(); ++y) {
                theater = theaters.get(y);
                li_array[y] = li(a(String.format(hyperLink, cinema.getId(), theater.getId()),
                        theater.getName()));
            }
            tableColumns = new String[]{"Title", "Release Year", "Duration"};
            td = new Writable[movies.size()][tableColumns.length];
            th = new Writable[tableColumns.length];
            for (int i = 0; i < tableColumns.length; i++) {
                th[i] = th(text(tableColumns[i]));
            }

            td_array = new Writable[movies.size() + 1];
            td_array[0] = tr(th);
            hyperLink = new GetMovieID().getPath()
                    .replace(MOVIE_ID_FULL.toString(), "%s"); //get path and make it ready to add ID

            HtmlViewCommon.fillTableDataMovies(movies, td, td_array, hyperLink);

            hyperLink = new GetCinemaIDSessionsToday().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%s"); //get path and make it ready to add ID

            String hyperLink_post = new GetCinemaIDTheaters().getPath()
                    .replace(CINEMA_ID_FULL.toString(), String.valueOf(cinema.getId()));

            return new HtmlPage("Cinema " + cinema.getName(),
                    h3(a(new GetCinemas().getPath(), "Cinemas")),
                    h1(text("Cinema " + cinema.getName())),
                    li(text("City: " + cinema.getCity())),
                    h2(text("Theaters: ")),
                    multipleElems(li_array),
                    breakLine(),
                    form(POST.toString(), hyperLink_post,
                            text("Theater name:"), breakLine(),
                            textInput(NAME.toString()), breakLine(),
                            text("Number of rows:"), breakLine(),
                            textInput(ROWS.toString()), breakLine(),
                            text("Number of seats per row:"), breakLine(),
                            textInput(SEATS_ROW.toString()), breakLine(),
                            submit("Create")
                    ),
                    h2(text("Movies: ")),
                    table(td_array),
                    h3(a(String.format(hyperLink, cinema.getId()), "Sessions today"))
            );
        }
        else
            return new InfoNotFoundView(data).createPage(); //TODO: will return wrong status code???
    }

    @Override
    public HttpStatusCode getCode() {
        return HttpStatusCode.OK;
    }
}