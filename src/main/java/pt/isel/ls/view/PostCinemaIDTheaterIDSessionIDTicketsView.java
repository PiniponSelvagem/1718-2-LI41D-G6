package pt.isel.ls.view;

import pt.isel.ls.core.common.commands.GetCinemaIDTheaterIDSessionIDTicketID;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_TID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SID;

public class PostCinemaIDTheaterIDSessionIDTicketsView extends PostView {

    public PostCinemaIDTheaterIDSessionIDTicketsView(DataContainer data) {
        super(data);
    }

    @Override
    protected void htmlRedirectHendle(HtmlPage header, PostData postData) {
        String hyperLink = new GetCinemaIDTheaterIDSessionIDTicketID().getPath()
                .replace(CINEMA_ID_FULL.toString(),  String.valueOf(data.getData(D_CID)))
                .replace(THEATER_ID_FULL.toString(), String.valueOf(data.getData(D_TID)))
                .replace(SESSION_ID_FULL.toString(), String.valueOf(data.getData(D_SID)))
                .replace(TICKET_ID_FULL.toString(),  postData.getId().toString());
        header.createRedirect(hyperLink);
    }
}
