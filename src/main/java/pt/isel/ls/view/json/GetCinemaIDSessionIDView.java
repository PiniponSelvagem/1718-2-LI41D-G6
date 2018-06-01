package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessionIDView extends JsonView {

    public GetCinemaIDSessionIDView(DataContainer data) {
        super(data);
    }

    @Override
    protected void createJson() {
        Session session = (Session) data.getData(D_SESSION);
        Movie movie = (Movie) data.getData(D_MOVIE);
        Theater theater = (Theater) data.getData(D_THEATER);
        if (session!=null && movie!=null && theater!=null)
            json.addObject(
                    new String[]{"Date", "Title", "Duration", "Theater name", "Available seats"},
                    new String[]{session.getDateTime(),
                            movie.getTitle(),
                            String.valueOf(movie.getDuration()),
                            theater.getName(),
                            String.valueOf(session.getAvailableSeats())}
            );
        else
            setInfoNotFound();
    }
}