package pt.isel.ls.view.html;

import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.common.commands.GetCinemaIDSessionID;
import pt.isel.ls.core.common.commands.GetMovies;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.view.html.utils.HtmlViewCommon;

import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetMovieIDView extends HtmlView {

    public GetMovieIDView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        HashMap<String, Cinema> cinemasMap = new HashMap<>();
        Movie movie = (Movie) data.getData(D_MOVIE);
        LinkedList<Cinema> cinemas = (LinkedList<Cinema>) data.getData(D_CINEMAS);
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);

        if (movie!=null) {
            String[] tableColumns = {"Name", "City"};
            Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);

            Writable[][] td1 = new Writable[cinemas.size()][tableColumns.length];
            Writable[] td_array1 = new Writable[cinemas.size() + 1];
            td_array1[0] = tr(th);
            Cinema cinema;
            String hyperLink_cinema = new GetCinemaID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%s");
            for (int i = 0; i < cinemas.size(); i++) {
                cinema = cinemas.get(i);
                cinemasMap.put(cinema.getId(), cinema);
                td1[i][0] = td(a(String.format(hyperLink_cinema, cinema.getId()), cinema.getName()));
                td1[i][1] = td(text(cinema.getCity()));
                td_array1[i + 1] = tr(td1[i]);
            }
            tableColumns = new String[]{"Session", "Cinema", "City", "Date"};
            th = new Writable[tableColumns.length];
            for (int i = 0; i < tableColumns.length; ++i) {
                th[i] = th(text(tableColumns[i]));
            }
            Writable[][] td2 = new Writable[sessions.size()][tableColumns.length];
            Writable[] td_array2 = new Writable[sessions.size() + 1];
            td_array2[0] = tr(th);
            Session session;
            String hyperLink_session = new GetCinemaIDSessionID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%s")
                    .replace(SESSION_ID_FULL.toString(), "%s");
            String hyperLink_movies = new GetMovies().getPath()
                    .replace(MOVIE_ID_FULL.toString(), "%s");
            String hyperLink_date = new GetCinemaIDSessionID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%s")
                    .replace(SESSION_ID_FULL.toString(), "%s"); //get path and make it ready to add IDs
            for (int i = 0; i < sessions.size(); i++) {
                session = sessions.get(i);
                cinema = cinemasMap.get(session.getCinemaID());
                td2[i][0] = td(a(String.format(hyperLink_session, cinema.getId(), session.getId()), session.getId()));
                td2[i][1] = td(a(String.format(hyperLink_cinema, cinema.getId()), cinema.getName()));
                td2[i][2] = td(text(cinema.getCity()));
                td2[i][3] = td(a(String.format(hyperLink_date, cinema.getId(), session.getId()), session.getDateTime()));
                td_array2[i + 1] = tr(td2[i]);
            }

            return new HtmlPage("Movie: " + movie.getTitle(),
                    h3(a(hyperLink_movies, "Movies")),
                    h1(text("Movie " + movie.getTitle())),
                    li(text("Release year: " + Integer.toString(movie.getYear()))),
                    li(text("Duration: " + Integer.toString(movie.getDuration()) + " minutes")),
                    h2(text("Cinemas playing " + movie.getTitle() + " :")),
                    table(td_array1),
                    h2(text("Movie Sessions: ")),
                    table(td_array2)
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
