package pt.isel.ls.view.command;

import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.common.commands.GetCinemaIDSessionID;
import pt.isel.ls.core.common.commands.GetCinemaIDTheaterID;
import pt.isel.ls.core.common.commands.GetMovieID;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.*;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.CommandEnum.CINEMAS;
import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


public class GetCinemaIDTheaterIDSessionIDTicketIDView extends CommandView {

    public GetCinemaIDTheaterIDSessionIDTicketIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        Ticket ticket = (Ticket) data.getData(D_TICKET);
        header.addDetailed("Cinema " + ticket.getSession().getCinemaID() + " - Theater " +
                        ticket.getSession().getTheater().getId() + " - Session " + ticket.getSession().getId() + " - Ticket " +ticket.getId(),
                new String[]{"Date", "Title", "Duration", "Theater name"},
                new String[]{ticket.getSession().getDateTime(),
                        ticket.getSession().getMovie().getTitle(),
                        String.valueOf(ticket.getSession().getMovie().getDuration()),
                        String.valueOf(ticket.getSession().getTheater().getName()),
                        String.valueOf(ticket.getRow()+""+ticket.getSeat())}
        );

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        Ticket ticket = (Ticket) data.getData(D_TICKET);
        Session session = (Session) data.getData(D_SESSION);
        Movie movie = (Movie) data.getData(D_MOVIE);
        Theater theater = (Theater) data.getData(D_THEATER);
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
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

        header = new HtmlPage("Ticket",
                h1(text("TICKET: " + ticket.getId())),
                h2(a(hyperlink_cinema, "Cinema: "+cinema.getName())),
                h2(a(hyperlink_theater, "Theater: "+theater.getName())),
                h2(a(hyperlink_session, "Session: "+session.getDateTime())),
                h2(a(hyperlink_movie, "Movie: "+movie.getTitle()))
        );
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        Ticket ticket = (Ticket) data.getData(D_TICKET);
        header.addObject(
                new String[]{"date", "title", "duration", "theater_name"},
                new String[]{ticket.getSession().getDateTime(),
                        ticket.getSession().getMovie().getTitle(),
                        String.valueOf(ticket.getSession().getMovie().getDuration()),
                        String.valueOf(ticket.getSession().getTheater().getName()),
                        String.valueOf(ticket.getRow()+""+ticket.getSeat())}
        );

        return header.getBuildedString();
    }
}