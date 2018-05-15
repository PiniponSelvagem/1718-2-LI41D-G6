package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;

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

        Writable[][] td = new Writable[session.getTheater().getRows()+1][session.getTheater().getSeatsPerRow()+1];
        Writable[] td_array = new Writable[session.getTheater().getRows()+1];
        td[0][0]=td(text("  "));
        for (int i = 1; i <= session.getTheater().getSeatsPerRow(); i++) {
            td[0][i] = td(text(""+i));
        }
        td_array[0] = tr(td[0]);
        for (int i = 1; i <= session.getTheater().getRows(); i++) {
            td[i][0]=td(text(""+(char)((i-1)+'A')));
            for(int j = 1; j <= session.getTheater().getSeatsPerRow(); j++) {
                tkid=""+(char)((i-1)+'A')+j;
                if(tickets.contains(tkid));
                td[i][j] = td(a("" +
                        DIR_SEPARATOR + CINEMAS + DIR_SEPARATOR + session.getCinemaID() + DIR_SEPARATOR + THEATERS + DIR_SEPARATOR
                        + session.getTheater().getId() + DIR_SEPARATOR + SESSIONS + DIR_SEPARATOR + session.getId()
                        + DIR_SEPARATOR + TICKETS + DIR_SEPARATOR +tkid,tkid ));
            }
            td_array[i] = tr(td[i]);
        }

        header = new HtmlPage("Session " + session.getDateTime(),
                h3(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+session.getCinemaID()+
                                DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+session.getTheater().getId(),
                        "Theater: "+theater.getName())),
                h2(text("Available Seats: "+ (int)this.data.getData(D_AVAILABLE_SEATS))),
                h2(text("Seats Display: ")),
                table(td_array)
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