package pt.isel.ls.view.html;

import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID_FULL;

public class PostCinemasView extends PostView {

    public PostCinemasView(DataContainer data) {
        super(data);
    }

    @Override
    public String createRedirect() {
        return new GetCinemaID().getPath()
                .replace(CINEMA_ID_FULL.toString(), postData.getId().toString());
    }
}
