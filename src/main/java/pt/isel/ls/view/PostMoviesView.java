package pt.isel.ls.view;

import pt.isel.ls.core.common.commands.GetMovieID;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.MOVIE_ID_FULL;

public class PostMoviesView extends PostView {

    public PostMoviesView(DataContainer data) {
        super(data);
    }

    @Override
    protected void htmlRedirectHendle(HtmlPage header, PostData postData) {
        String hyperLink = new GetMovieID().getPath()
                .replace(MOVIE_ID_FULL.toString(), postData.getId().toString());
        header.createRedirect(hyperLink);
    }
}
