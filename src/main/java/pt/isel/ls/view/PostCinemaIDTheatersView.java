package pt.isel.ls.view;

import pt.isel.ls.core.common.commands.GetCinemaIDTheaterID;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID_FULL;
import static pt.isel.ls.core.strings.CommandEnum.THEATER_ID_FULL;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;

public class PostCinemaIDTheatersView extends PostView {

    public PostCinemaIDTheatersView(DataContainer data) {
        super(data);
    }

    @Override
    protected void htmlRedirectHendle(HtmlPage header, PostData postData) {
        String hyperLink = new GetCinemaIDTheaterID().getPath()
                .replace(CINEMA_ID_FULL.toString(), String.valueOf(data.getData(D_CID)))
                .replace(THEATER_ID_FULL.toString(), postData.getId().toString());
        header.createRedirect(hyperLink);
    }
}
