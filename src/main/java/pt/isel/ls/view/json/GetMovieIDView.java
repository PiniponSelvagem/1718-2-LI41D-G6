package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.*;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetMovieIDView extends JsonView {

    public GetMovieIDView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        Movie movie = (Movie) data.getData(D_MOVIE);
        json.addObject(
                new String[]{"title", "year", "duration"},
                new String[]{movie.getTitle(),
                        String.valueOf(movie.getYear()),
                        String.valueOf(movie.getDuration())}
        );
    }
}
