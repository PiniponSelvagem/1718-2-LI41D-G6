package pt.isel.ls.view.html;

import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.common.commands.*;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.html.utils.HtmlViewCommon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessionsDateIDView extends HtmlView {
    private Date date;
    private static final int FULL_DAY = 24*60*60*1000;

    public GetCinemaIDSessionsDateIDView(DataContainer data) {
        super(data);
        this.date = (Date) data.getData(D_DATE);
    }

    @Override
    public HtmlPage createPage() {
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
        HashMap<String, Theater> theaters = (HashMap<String, Theater>) data.getData(D_THEATERS);
        HashMap<String, Movie> movies = (HashMap<String, Movie>) data.getData(D_MOVIES);
        Cinema cinema = (Cinema) data.getData(D_CINEMA);

        if (cinema!=null) {
            String[] tableColumns = {"Session Time", "Theater", "Movie", "Number Of Seats", "Available Seats"};
            Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);

            Writable[][] td = new Writable[sessions.size() + 1][tableColumns.length];
            Writable[] td_array = new Writable[sessions.size() + 1];
            td_array[0] = tr(th);

            Session session;
            String hyperlink_date = new GetCinemaIDSessionID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%s")
                    .replace(SESSION_ID_FULL.toString(), "%s");
            String hyperlink_theater = new GetCinemaIDTheaterID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%s")
                    .replace(THEATER_ID_FULL.toString(), "%s");
            String hyperlink_movie = new GetMovieID().getPath()
                    .replace(MOVIE_ID_FULL.toString(), "%s");
            for (int i = 0; i < sessions.size(); ++i) {
                session = sessions.get(i);
                td[i][0] = td(a(String.format(hyperlink_date, session.getCinemaID(), session.getId()),
                        session.getTime())
                );
                td[i][1] = td(a(String.format(hyperlink_theater, session.getCinemaID(),
                        theaters.get(session.getTheaterID()).getId()),
                        theaters.get(session.getTheaterID()).getName())
                );
                td[i][2] = td(a(String.format(hyperlink_movie, session.getMovieID()),
                        movies.get(session.getMovieID()).getTitle()));
                td[i][3] = td(text(String.valueOf(theaters.get(session.getTheaterID()).getSeats())));
                td[i][4] = td(text(String.valueOf(session.getAvailableSeats())));
                td_array[i + 1] = tr(td[i]);
            }

            SimpleDateFormat sdf_withSep = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf_noSep = new SimpleDateFormat("ddMMyyyy");

            String tomorrow = sdf_noSep.format(new Date(this.date.getTime() + FULL_DAY));
            String yesterday = sdf_noSep.format(new Date(this.date.getTime() - FULL_DAY));

            String hyperlink_dateNavigator = new GetCinemaIDSessionsDateID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), cinema.getId())
                    .replace(DATE_ID_FULL.toString(), "%s");

            String hyperlink_cinema = new GetCinemaID().getPath().replace(CINEMA_ID_FULL.toString(), "%s");

            return new HtmlPage("Sessions for date: " + sdf_withSep.format(this.date),
                    h1(text("Sessions for date: " + sdf_withSep.format(this.date))),
                    h3(a(String.format(hyperlink_cinema, cinema.getId()), cinema.getName())),
                    h3(a(String.format(hyperlink_dateNavigator, yesterday), "Previous day")),
                    h3(a(String.format(hyperlink_dateNavigator, tomorrow), "Next day")),
                    table(td_array)
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