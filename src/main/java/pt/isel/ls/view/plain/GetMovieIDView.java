package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetMovieIDView extends PlainView {

    public GetMovieIDView(DataContainer data) {
        super(data);
    }

    @Override
    protected void createPlain() {
        Movie movie = (Movie) data.getData(D_MOVIE);
        plain.addDetailed("Movie "+movie.getId(),
                new String[]{"Title", "Year", "Duration"},
                new String[]{movie.getTitle(),
                        String.valueOf(movie.getYear()),
                        String.valueOf(movie.getDuration())}
        );
    }
}
