package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessionIDView extends PlainView {

    public GetCinemaIDSessionIDView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        Session session = (Session) data.getData(D_SESSION);
        Movie movie = (Movie) data.getData(D_MOVIE);
        Theater theater = (Theater) data.getData(D_THEATER);
        if (session!=null && movie!=null && theater!=null)
            plain.addDetailed("Cinema "+session.getCinemaID()+" - Session "+session.getId(),
                    new String[]{"Date", "Title", "Duration", "Theater name", "Available seats"},
                    new String[]{session.getDateTime(),
                            movie.getTitle(),
                            String.valueOf(movie.getDuration()),
                            theater.getName(),
                            String.valueOf(session.getAvailableSeats())}
            );
        else
            plain.addTitle(infoNotFound);
    }
}