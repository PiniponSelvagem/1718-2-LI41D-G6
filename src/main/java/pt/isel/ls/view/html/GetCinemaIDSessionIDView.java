package pt.isel.ls.view.html;

import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.common.commands.*;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;

import java.util.HashMap;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.common.headers.html.HtmlElem.submit;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessionIDView extends HtmlView {

    public GetCinemaIDSessionIDView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        Session session = (Session) data.getData(D_SESSION);
        Movie movie = (Movie) data.getData(D_MOVIE);
        Theater theater = (Theater) data.getData(D_THEATER);
        HashMap<String, Ticket> tickets = (HashMap<String, Ticket>) data.getData(D_TICKETS);

        if (session!=null && movie!=null && theater!=null) {
            Writable[][] td = new Writable[theater.getRows()][theater.getSeatsPerRow()];
            Writable[] td_array = new Writable[theater.getRows()];

            String hyperlink_ticket = new GetCinemaIDTheaterIDSessionIDTicketID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), String.valueOf(session.getCinemaID()))
                    .replace(THEATER_ID_FULL.toString(), String.valueOf(theater.getId()))
                    .replace(SESSION_ID_FULL.toString(), String.valueOf(session.getId()))
                    .replace(TICKET_ID_FULL.toString(), "%s");

            String tkRow, tkSeat, tkid;
            String addTicketButton = "submit";
            String hyperlink_newTicket = new GetCinemaIDTheaterIDSessionIDTickets().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%d")
                    .replace(THEATER_ID_FULL.toString(), "%d")
                    .replace(SESSION_ID_FULL.toString(), "%d");
            for (int i = 0; i < theater.getRows(); ++i) {
                for (int j = 0; j < theater.getSeatsPerRow(); ++j) {
                    tkRow = ""+(char) ((i) + 'A');
                    tkSeat= ""+(j + 1);
                    tkid = tkRow+tkSeat;
                    if (tickets.get(tkid) != null) {
                        td[i][j] = tdCustom(a(String.format(hyperlink_ticket, tkid), tkid), HtmlPage.COLOR_RED_LIGHT);
                    } else {
                        td[i][j] = tdCustom(
                                form(POST.toString(), String.format(hyperlink_newTicket, session.getCinemaID(), theater.getId(), session.getId()),
                                        hiddenInput(SEATS_ROW.toString(), tkSeat),
                                        hiddenInput(ROWS.toString(), tkRow),
                                        submit(addTicketButton, tkid)
                                ),
                                HtmlPage.COLOR_GREEN_LIGHT);
                    }
                }
                td_array[i] = tr(td[i]);
            }

            String hyperlink_theater = new GetCinemaIDTheaterID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), String.valueOf(session.getCinemaID()))
                    .replace(THEATER_ID_FULL.toString(), String.valueOf(theater.getId()));

            String hyperlink_movie = new GetMovieID().getPath()
                    .replace(MOVIE_ID_FULL.toString(), String.valueOf(movie.getId()));

            String hyperlink_today = new GetCinemaIDSessionsToday().getPath()
                    .replace(CINEMA_ID_FULL.toString(), String.valueOf(session.getCinemaID()));

            String ticketsTable = "tickets_table";
            return new HtmlPage("Session " + session.getDateTime(), ticketsTable, addTicketButton,
                    h3(a(hyperlink_theater, "Theater: " + theater.getName())),
                    h3(a(hyperlink_movie, "Movie: " + movie.getTitle())),
                    h2(text("Date: " + session.getDateTime())),
                    h2(text("Movie: " + movie.getTitle())),
                    h2(text("Available Seats: " + session.getAvailableSeats())),
                    tableWithName(ticketsTable, td_array),
                    h3(a(hyperlink_today, "Sessions Today"))
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