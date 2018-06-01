package pt.isel.ls.view.html;

import pt.isel.ls.core.common.commands.GetCinemaIDTheaterIDSessionIDTicketID;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class PostCinemaIDTheaterIDSessionIDTicketsView extends PostView {

    public PostCinemaIDTheaterIDSessionIDTicketsView(DataContainer data) {
        super(data);
    }

    @Override
    public String createRedirect() {
        return new GetCinemaIDTheaterIDSessionIDTicketID().getPath()
                .replace(CINEMA_ID_FULL.toString(),  (String) data.getData(D_CID))
                .replace(THEATER_ID_FULL.toString(), (String) data.getData(D_TID))
                .replace(SESSION_ID_FULL.toString(), (String) data.getData(D_SID))
                .replace(TICKET_ID_FULL.toString(),  postData.getId().toString());
    }
}
