package pt.isel.ls.view;

import pt.isel.ls.apps.http_server.http.HttpStatusCode;
import pt.isel.ls.core.common.commands.*;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.utils.HtmlViewCommon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessionsDateIDView extends CommandView {
    private int cinemaId;
    private Date date;
    private static final int FULL_DAY = 24*60*60*1000;

    public GetCinemaIDSessionsDateIDView(DataContainer data) {
        super(data);
        this.cinemaId = (Integer) data.getData(D_CID);
        this.date = (Date) data.getData(D_DATE);
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Sessions (CinemaID: "+cinemaId+") [Date: "+date+"]");
        String[] tableColumns = {"ID", "Starting time", "Movie Title", "Duration", "Theater name", "Available seats"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(HtmlPage header) {
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
        HashMap<Integer, Theater> theaters = (HashMap<Integer, Theater>) data.getData(D_THEATERS);
        HashMap<Integer, Movie> movies = (HashMap<Integer, Movie>) data.getData(D_MOVIES);
        Cinema cinema = (Cinema) data.getData(D_CINEMA);

        if (cinema!=null) {
            String[] tableColumns = {"Session Time", "Theater", "Movie", "Number Of Seats", "Available Seats"};
            Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);

            Writable[][] td = new Writable[sessions.size() + 1][tableColumns.length];
            Writable[] td_array = new Writable[sessions.size() + 1];
            td_array[0] = tr(th);

            Session session;
            String hyperlink_date = new GetCinemaIDSessionID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%d")
                    .replace(SESSION_ID_FULL.toString(), "%d");
            String hyperlink_theater = new GetCinemaIDTheaterID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%d")
                    .replace(THEATER_ID_FULL.toString(), "%d");
            String hyperlink_movie = new GetMovieID().getPath()
                    .replace(MOVIE_ID_FULL.toString(), "%d");
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
                    .replace(CINEMA_ID_FULL.toString(), String.valueOf(cinemaId))
                    .replace(DATE_ID_FULL.toString(), "%s");

            String hyperlink_cinema = new GetCinemaID().getPath().replace(CINEMA_ID_FULL.toString(), "%d");

            header.createPage(HttpStatusCode.OK, "Sessions for date: " + sdf_withSep.format(this.date),
                    h1(text("Sessions for date: " + sdf_withSep.format(this.date))),
                    h3(a(String.format(hyperlink_cinema, cinemaId), cinema.getName())),
                    h3(a(String.format(hyperlink_dateNavigator, yesterday), "Previous day")),
                    h3(a(String.format(hyperlink_dateNavigator, tomorrow), "Next day")),
                    table(td_array)
            );
        }
        else
            return new InfoNotFoundView(data).toHtml(header);

        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "start_time", "movie_title", "duration", "theater_name", "available_seats"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
        HashMap<Integer, Theater> theaters = (HashMap<Integer, Theater>) data.getData(D_THEATERS);
        HashMap<Integer, Movie> movies = (HashMap<Integer, Movie>) data.getData(D_MOVIES);
        String[][] tableData  = new String[sessions.size()][columnNames.length];
        Session session;
        for (int y=0; y<sessions.size(); ++y) {
            session = sessions.get(y);
            tableData[y][0] = String.valueOf(session.getId());
            tableData[y][1] = session.getDateTime();
            tableData[y][2] = String.valueOf(movies.get(session.getMovieID()).getTitle());
            tableData[y][3] = String.valueOf(movies.get(session.getMovieID()).getDuration());
            tableData[y][4] = String.valueOf(theaters.get(session.getTheaterID()).getName());
            tableData[y][5] = String.valueOf(session.getAvailableSeats());
        }
        return tableData;
    }
}