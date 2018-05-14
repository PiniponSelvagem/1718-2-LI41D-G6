package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;

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
        String[] tableColumns = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18",
                "19","20","21","22","23","24","25","26","27","28","29","30","31","32","32","33","34"};
        String[] tablelines = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R",
                "S","T","U","V","W","X","Y","Z"};
        Writable[][] td = new Writable[session.getTheater().getSeatsPerRow()][session.getTheater().getRows()];
        Writable[] th = new Writable[session.getTheater().getRows()];
        for (int i = 0; i < session.getTheater().getRows(); i++) {
            th[i] = th(text(tableColumns[i]));
        }
        Writable[] td_array = new Writable[session.getTheater().getRows()+1];
        td_array[0] = tr(th);
        for (int j = 0; j < session.getTheater().getRows(); j++) {
            for(int i = 0; i < session.getTheater().getSeatsPerRow(); i++) {
                td[j][i] = td(a("" +
                        DIR_SEPARATOR + CINEMAS + DIR_SEPARATOR + session.getCinemaID() + DIR_SEPARATOR + THEATERS + DIR_SEPARATOR
                        + session.getTheater().getId() + DIR_SEPARATOR + SESSIONS + DIR_SEPARATOR + session.getId()
                        + DIR_SEPARATOR + TICKETS + DIR_SEPARATOR +tablelines[j]+tableColumns[i], tablelines[j]+tableColumns[i]));
                td_array[j + 1] = tr(td[j]);
            }
        }

        header = new HtmlPage("Session" + session.getDateTime(),
                h3(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+session.getCinemaID()+DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+
                        session.getTheater().getId()+DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+session.getId(),
                        "Session:"+session.getDateTime())),
                h2(text("Ticket: "+ ticket.getId())),
                h2(text("Seats Display: ")),
                table(td_array)
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