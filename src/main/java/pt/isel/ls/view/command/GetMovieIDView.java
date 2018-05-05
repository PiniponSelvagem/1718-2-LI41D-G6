package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;

public class GetMovieIDView extends CommandView {

    public GetMovieIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected void allInfo() {
        Header header = data.getHeader();

        if (header != null) {
            Movie movie = (Movie) data.getData(0);
            header.addObject("Movie "+movie.getId(),
                    new String[]{"Title", "Year", "Duration"},
                    new String[]{movie.getTitle(),
                            String.valueOf(movie.getYear()),
                            String.valueOf(movie.getDuration())}
            );

            header.close();
            header.writeToFile();

            infoString = header.getBuildedString();
        }
    }
}
