package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.Html;
import pt.isel.ls.core.common.headers.Json;
import pt.isel.ls.core.common.headers.Plain;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIE;

public class GetMovieIDView extends CommandView {

    public GetMovieIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        Movie movie = (Movie) data.getData(D_MOVIE);
        header.addDetailed("Movie "+movie.getId(),
                new String[]{"Title", "Year", "Duration"},
                new String[]{movie.getTitle(),
                        String.valueOf(movie.getYear()),
                        String.valueOf(movie.getDuration())}
        );

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        Movie movie = (Movie) data.getData(D_MOVIE);
        header.addObject(
                new String[]{"title", "year", "duration"},
                new String[]{movie.getTitle(),
                        String.valueOf(movie.getYear()),
                        String.valueOf(movie.getDuration())}
        );

        return header.getBuildedString();
    }
}
