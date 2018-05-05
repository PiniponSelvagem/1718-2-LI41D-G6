package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Ticket;


public class GetCinemaIDTheaterIDSessionIDTicketIDView extends CommandView {

    public GetCinemaIDTheaterIDSessionIDTicketIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected void allInfo() {
        Header header = data.getHeader();

        if (header != null) {
            Ticket ticket = (Ticket) data.getData(0);
            header.addObject("Cinema " + ticket.getSession().getCinemaID() + " - Theater " +
                    ticket.getSession().getTheater().getId() + " - Session " + ticket.getSession().getId() + " - Ticket " +ticket.getId(),
                    new String[]{"Date", "Title", "Duration", "Theater name"},
                    new String[]{ticket.getSession().getDateTime(),
                            ticket.getSession().getMovie().getTitle(),
                            String.valueOf(ticket.getSession().getMovie().getDuration()),
                            String.valueOf(ticket.getSession().getTheater().getName()),
                            String.valueOf(ticket.getRow()+""+ticket.getSeat())}
            );

            header.close();
            header.writeToFile();

            infoString = header.getBuildedString();
        }
    }
}