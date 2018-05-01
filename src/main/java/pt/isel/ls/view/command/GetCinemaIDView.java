package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;

public class GetCinemaIDView extends CommandView {

    public GetCinemaIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected void allInfo() {
        Header header = data.getHeader();

        if (header != null) {
            Cinema cinema = (Cinema) data.getData(0);
            header.addObject("Cinema "+cinema.getId(),
                    new String[]{"Name", "City"},
                    new String[]{cinema.getName(), cinema.getCity()}
            );

            header.close();
            header.writeToFile();

            infoString = header.getBuildedString();
        }
    }
}