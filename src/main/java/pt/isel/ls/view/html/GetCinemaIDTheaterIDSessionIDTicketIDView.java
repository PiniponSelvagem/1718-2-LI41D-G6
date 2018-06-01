package pt.isel.ls.view.html;

import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.common.commands.GetCinemaIDSessionID;
import pt.isel.ls.core.common.commands.GetCinemaIDTheaterID;
import pt.isel.ls.core.common.commands.GetMovieID;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.*;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


public class GetCinemaIDTheaterIDSessionIDTicketIDView extends HtmlView {

    public GetCinemaIDTheaterIDSessionIDTicketIDView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        Ticket ticket = (Ticket) data.getData(D_TICKET);
        Session session = (Session) data.getData(D_SESSION);
        Movie movie = (Movie) data.getData(D_MOVIE);
        Theater theater = (Theater) data.getData(D_THEATER);
        Cinema cinema = (Cinema) data.getData(D_CINEMA);

        if (ticket!=null && session!=null && theater!=null && movie!=null && cinema!=null) {
            String hyperlink_cinema = new GetCinemaID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), String.valueOf(cinema.getId()));
            String hyperlink_theater = new GetCinemaIDTheaterID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), String.valueOf(cinema.getId()))
                    .replace(THEATER_ID_FULL.toString(), String.valueOf(theater.getId()));
            String hyperlink_session = new GetCinemaIDSessionID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), String.valueOf(cinema.getId()))
                    .replace(SESSION_ID_FULL.toString(), String.valueOf(session.getId()));
            String hyperlink_movie = new GetMovieID().getPath()
                    .replace(MOVIE_ID_FULL.toString(), String.valueOf(movie.getId()));

            return new HtmlPage("Ticket",
                    h1(text("TICKET: " + ticket.getId())),
                    h2(a(hyperlink_cinema, "Cinema: " + cinema.getName())),
                    h2(a(hyperlink_theater, "Theater: " + theater.getName())),
                    h2(a(hyperlink_session, "Session: " + session.getDateTime())),
                    h2(a(hyperlink_movie, "Movie: " + movie.getTitle()))
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