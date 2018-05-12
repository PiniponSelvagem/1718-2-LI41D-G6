package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Ticket;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_TICKET;


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
        return super.toHtml(header);
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