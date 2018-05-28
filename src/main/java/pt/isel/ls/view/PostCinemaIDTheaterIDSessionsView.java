package pt.isel.ls.view;

import pt.isel.ls.core.common.commands.GetCinemaIDSessionID;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID_FULL;
import static pt.isel.ls.core.strings.CommandEnum.SESSION_ID_FULL;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;

public class PostCinemaIDTheaterIDSessionsView extends PostView {

    public PostCinemaIDTheaterIDSessionsView(DataContainer data) {
        super(data);
    }

    @Override
    protected void htmlRedirectHendle(HtmlPage header, PostData postData) {
        String hyperLink = new GetCinemaIDSessionID().getPath()
                .replace(CINEMA_ID_FULL.toString(), String.valueOf(data.getData(D_CID)))
                .replace(SESSION_ID_FULL.toString(), postData.getId().toString());
        header.createRedirect(hyperLink);
    }
}
