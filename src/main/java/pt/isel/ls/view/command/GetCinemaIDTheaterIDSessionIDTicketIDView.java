package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.*;

import java.util.LinkedList;

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
        Movie movie=(Movie) data.getData(D_MOVIE);
        Theater theater=(Theater) data.getData(D_THEATER);
        Cinema cinema=(Cinema) data.getData(D_CINEMA);
        header = new HtmlPage("TICKET: " + ticket.getId(),
                h2(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+cinema.getId(),
                        "Cinema: "+cinema.getName())),
                h2(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+cinema.getName()+DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+
                                theater.getId(),
                        "Theater: "+theater.getName())),
                h2(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+session.getCinemaID()+DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+
                                session.getTheater().getId()+DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+session.getId(),
                        "Session: "+session.getDateTime())),
                h2(a(""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+movie.getId(),
                        "Movie: "+movie.getTitle()))
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