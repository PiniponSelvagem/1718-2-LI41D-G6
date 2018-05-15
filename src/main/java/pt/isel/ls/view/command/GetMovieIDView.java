package pt.isel.ls.view.command;

import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.common.commands.GetCinemaIDSessionID;
import pt.isel.ls.core.common.commands.GetCinemaIDTheaterIDSessions;

import pt.isel.ls.core.common.headers.Html;
import pt.isel.ls.core.common.headers.Json;
import pt.isel.ls.core.common.headers.Plain;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.*;


import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID_FULL;
import static pt.isel.ls.core.strings.CommandEnum.SESSION_ID_FULL;
import static pt.isel.ls.core.strings.CommandEnum.THEATER_ID_FULL;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetMovieIDView extends CommandView {

    public GetMovieIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        Movie movie = (Movie) data.getData(D_MOVIE);
        header.addDetailed("Movie "+movie.getId(),
                new String[]{"Title", "Year", "Duration"},
                new String[]{movie.getTitle(),
                        String.valueOf(movie.getYear()),
                        String.valueOf(movie.getDuration())}
        );

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        HashMap<Integer, Cinema> cinemasMap = new HashMap<>();
        Movie movie = (Movie)data.getData(D_MOVIE);
        LinkedList<Cinema> cinemas = (LinkedList<Cinema>) data.getData(D_CINEMAS);
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
        //LinkedList<Cinema> cinemas2 = (LinkedList<Cinema>) data.getData(DC_CINEMAS);

        String[] tableColumns = {"Name", "City"};
        Writable[] th = new Writable[tableColumns.length];
        for (int i=0; i<tableColumns.length; ++i) {
            th[i] = th(text(tableColumns[i]));
        }
        Writable[][] td1 =  new Writable[cinemas.size()][tableColumns.length];
        Writable[] td_array1 = new Writable[cinemas.size()+1];
        td_array1[0] = tr(th);
        Cinema cinema;
        String hyperLink = new GetCinemaID().getPath()
                .replace(CINEMA_ID_FULL.toString(), "%d");
        for (int i = 0; i < cinemas.size(); i++) {
            cinema = cinemas.get(i);
            cinemasMap.put(cinema.getId(), cinema);
            td1[i][0] = td(a(String.format(hyperLink, cinema.getId()),cinema.getName()));
            td1[i][1] = td(text(cinema.getCity()));
            td_array1[i+1] = tr(td1[i]);
        }
        tableColumns = new String[]{"Session", "Cinema", "City", "Date"};
        th = new Writable[tableColumns.length];
        for (int i=0; i<tableColumns.length; ++i) {
            th[i] = th(text(tableColumns[i]));
        }
        Writable[][] td2 = new Writable[sessions.size()][tableColumns.length];
        Writable[] td_array2 = new Writable[sessions.size()+1];
        td_array2[0] = tr(th);
        Session session;
        hyperLink = new GetCinemaIDSessionID().getPath()
                .replace(CINEMA_ID_FULL.toString(), "%d")
                .replace(SESSION_ID_FULL.toString(), "%d");
        for (int i = 0; i < sessions.size(); i++) {
            session = sessions.get(i);
            cinema = cinemasMap.get(session.getCinemaID());
            td2[i][0] = td(a(String.format(hyperLink,cinema.getId(), session.getId()), Integer.toString(session.getId())));
            td2[i][1] = td(text(cinema.getName()));
            td2[i][2] = td(text(cinema.getCity()));
            td2[i][3] = td(text(session.getDateTime()));
            td_array2[i+1] = tr(td2[i]);
        }

        header = new HtmlPage("Movie " + movie.getTitle(),
                h1(text("Movie " + movie.getTitle())),
                li(text("Release year: "+Integer.toString(movie.getYear()))),
                li(text("Duration: "+Integer.toString(movie.getDuration())+" minutes")),
                h2(text("Cinemas playing "+movie.getTitle()+" :")),
                table(td_array1),
                h2(text("Movie Sessions: ")),
                table(td_array2)
        );
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        Movie movie = (Movie) data.getData(D_MOVIE);
        header.addObject(
                new String[]{"title", "year", "duration"},
                new String[]{movie.getTitle(),
                        String.valueOf(movie.getYear()),
                        String.valueOf(movie.getDuration())}
        );

        return header.getBuildedString();
    }
}
