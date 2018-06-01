package pt.isel.ls.view.html;

import pt.isel.ls.core.common.commands.GetMovieID;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.MOVIE_ID_FULL;

public class PostMoviesView extends PostView {

    public PostMoviesView(DataContainer data) {
        super(data);
    }

    @Override
    public String createRedirect() {
        return new GetMovieID().getPath()
                .replace(MOVIE_ID_FULL.toString(), postData.getId().toString());
    }
}
