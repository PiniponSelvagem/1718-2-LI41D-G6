package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.*;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


public class GetCinemaIDTheaterIDSessionIDTicketIDView extends PlainView {

    public GetCinemaIDTheaterIDSessionIDTicketIDView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        Ticket ticket = (Ticket) data.getData(D_TICKET);
        Session session = (Session) data.getData(D_SESSION);
        Theater theater = (Theater) data.getData(D_THEATER);
        Movie movie = (Movie) data.getData(D_MOVIE);
        if (ticket!=null && session!=null && theater!=null && movie!=null)
            plain.addDetailed("Cinema " + session.getCinemaID() + " - Theater " +
                            theater.getId() + " - Session " + ticket.getSessionID() + " - Ticket " +ticket.getId(),
                    new String[]{"Date", "Title", "Duration", "Theater name"},
                    new String[]{session.getDateTime(),
                            movie.getTitle(),
                            String.valueOf(movie.getDuration()),
                            theater.getName(),
                            ticket.getRow()+""+ticket.getSeat()}
            );
        else
            plain.addTitle(infoNotFound);
    }
}