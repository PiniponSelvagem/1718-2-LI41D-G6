package pt.isel.ls.view.html;

import pt.isel.ls.core.common.commands.GetCinemaIDTheaterID;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID_FULL;
import static pt.isel.ls.core.strings.CommandEnum.THEATER_ID_FULL;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;

public class PostCinemaIDTheatersView extends PostView {

    public PostCinemaIDTheatersView(DataContainer data) {
        super(data);
    }

    @Override
    public String createRedirect() {
        return new GetCinemaIDTheaterID().getPath()
                .replace(CINEMA_ID_FULL.toString(), (String) data.getData(D_CID))
                .replace(THEATER_ID_FULL.toString(), postData.getId().toString());
    }
}
