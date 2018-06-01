package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.*;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


public class GetCinemaIDTheaterIDSessionIDTicketIDView extends JsonView {

    public GetCinemaIDTheaterIDSessionIDTicketIDView(DataContainer data) {
        super(data);
    }

    @Override
    protected void createJson() {
        Ticket ticket = (Ticket) data.getData(D_TICKET);
        Session session = (Session) data.getData(D_SESSION);
        Theater theater = (Theater) data.getData(D_THEATER);
        Movie movie = (Movie) data.getData(D_MOVIE);
        if (ticket!=null && session!=null && theater!=null && movie!=null)
            json.addObject(
                    new String[]{"date", "title", "duration", "theater_name"},
                    new String[]{session.getDateTime(),
                            movie.getTitle(),
                            String.valueOf(movie.getDuration()),
                            theater.getName(),
                            ticket.getRow()+""+ticket.getSeat()}
            );
        else
            setInfoNotFound();
    }
}