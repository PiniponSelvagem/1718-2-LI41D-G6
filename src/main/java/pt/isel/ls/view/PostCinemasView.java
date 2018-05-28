package pt.isel.ls.view;

import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID_FULL;

public class PostCinemasView extends PostView {

    public PostCinemasView(DataContainer data) {
        super(data);
    }

    @Override
    protected void htmlRedirectHendle(HtmlPage header, PostData postData) {
        String hyperLink = new GetCinemaID().getPath()
                .replace(CINEMA_ID_FULL.toString(), postData.getId().toString());
        header.createRedirect(hyperLink);
    }
}
