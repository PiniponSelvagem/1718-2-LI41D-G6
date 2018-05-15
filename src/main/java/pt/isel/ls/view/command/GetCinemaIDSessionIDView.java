package pt.isel.ls.view.command;

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
import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
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
                        String.valueOf(session.getTheater().getAvailableSeats())}
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
                    td[i][j] = td(a(String.format(hyperLink, tkid), tkid));
                }
                else {
                    td[i][j] = td(text(tkid));
                }
            }
            td_array[i] = tr(td[i]);
        }

        hyperLink = new GetCinemaIDTheaterID().getPath()
                .replace(CINEMA_ID_FULL.toString(), String.valueOf(session.getCinemaID()))
                .replace(THEATER_ID_FULL.toString(), String.valueOf(session.getTheater().getId()));

        String ticketsTable = "tickets_table";
        header = new HtmlPage("Session " + session.getDateTime(), ticketsTable,
                h3(a(hyperLink, "Theater: "+theater.getName())),
                h2(text("Available Seats: "+ (int)this.data.getData(D_AVAILABLE_SEATS))),
                h2(text("Seats Display: ")),
                tableWithName(ticketsTable, td_array)
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
                        String.valueOf(session.getTheater().getAvailableSeats())}
        );

        return header.getBuildedString();
    }
}