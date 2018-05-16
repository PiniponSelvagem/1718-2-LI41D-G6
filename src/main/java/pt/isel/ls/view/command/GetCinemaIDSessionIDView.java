package pt.isel.ls.view.command;

import pt.isel.ls.core.common.commands.GetCinemaIDSessionsToday;
import pt.isel.ls.core.common.commands.GetCinemaIDTheaterID;
import pt.isel.ls.core.common.commands.GetCinemaIDTheaterIDSessionIDTicketID;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessionIDView extends CommandView {

    public GetCinemaIDSessionIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        Session session = (Session) data.getData(D_SESSIONS);
        header.addDetailed("Cinema "+session.getCinemaID()+" - Session "+session.getId(),
                new String[]{"Date", "Title", "Duration", "Theater name", "Available seats"},
                new String[]{session.getDateTime(),
                        session.getMovie().getTitle(),
                        String.valueOf(session.getMovie().getDuration()),
                        session.getTheater().getName(),
                        String.valueOf(session.getAvailableSeats())}
        );

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        Session session=(Session) data.getData(D_SESSION);
        Theater theater= (Theater) data.getData(D_THEATER);
        LinkedList<String> tickets= (LinkedList<String>)data.getData(D_TICKETS);
        String tkid;

        Writable[][] td = new Writable[session.getTheater().getRows()][session.getTheater().getSeatsPerRow()];
        Writable[] td_array = new Writable[session.getTheater().getRows()];

        String hyperLink = new GetCinemaIDTheaterIDSessionIDTicketID().getPath()
                .replace(CINEMA_ID_FULL.toString(), String.valueOf(session.getCinemaID()))
                .replace(THEATER_ID_FULL.toString(), String.valueOf(session.getTheater().getId()))
                .replace(SESSION_ID_FULL.toString(), String.valueOf(session.getId()))
                .replace(TICKET_ID_FULL.toString(), "%s");

        for (int i = 0; i < session.getTheater().getRows(); ++i) {
            for(int j = 0; j < session.getTheater().getSeatsPerRow(); ++j) {
                tkid=""+(char)((i)+'A')+(j+1);
                if(tickets.contains(tkid)) {
                    td[i][j] = tdCustom(a(String.format(hyperLink, tkid), tkid), HtmlPage.COLOR_RED_LIGHT);
                }
                else {
                    td[i][j] = tdCustom(text(tkid), HtmlPage.COLOR_GREEN_LIGHT);
                }
            }
            td_array[i] = tr(td[i]);
        }
        String hl = new GetCinemaIDSessionsToday().getPath()
                .replace(CINEMA_ID_FULL.toString(), "%d");

        hyperLink = new GetCinemaIDTheaterID().getPath()
                .replace(CINEMA_ID_FULL.toString(), String.valueOf(session.getCinemaID()))
                .replace(THEATER_ID_FULL.toString(), String.valueOf(session.getTheater().getId()));

        String ticketsTable = "tickets_table";
        header = new HtmlPage("Session " + session.getDateTime(), ticketsTable,
                h3(a(hyperLink, "Theater: "+theater.getName())),
                h3(a(""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+session.getMovie().getId()
                        , "Movie: "+session.getMovie().getTitle())),
                h2(text("Available Seats: "+session.getAvailableSeats())),
                h2(text("Seats Display: ")),
                tableWithName(ticketsTable, td_array),
                h3(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+theater.getCinemaID()+DIR_SEPARATOR+SESSIONS+
                        DIR_SEPARATOR+TODAY, "Sessions Today"))
        );
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        Session session = (Session) data.getData(D_SESSIONS);
        header.addObject(
                new String[]{"Date", "Title", "Duration", "Theater name", "Available seats"},
                new String[]{session.getDateTime(),
                        session.getMovie().getTitle(),
                        String.valueOf(session.getMovie().getDuration()),
                        session.getTheater().getName(),
                        String.valueOf(session.getAvailableSeats())}
        );

        return header.getBuildedString();
    }
}