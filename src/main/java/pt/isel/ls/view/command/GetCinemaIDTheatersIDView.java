package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;

public class GetCinemaIDTheatersIDView extends CommandView {

    public GetCinemaIDTheatersIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected void allInfo() {
        Header header = data.getHeader();

        if (header != null) {
            Theater theater = (Theater) data.getData(0);
            header.addObject("Theater (ID: " + theater.getId()+") [Cinema ID: " + theater.getCinemaID()+"]",
                    new String[]{"Name", "Rows", "Seats per row", "Total seats"},
                    new String[]{theater.getName(),
                            String.valueOf(theater.getRows()),
                            String.valueOf(theater.getSeatsPerRow()),
                            String.valueOf(theater.getAvailableSeats())}
            );

            header.close();
            header.writeToFile();

            infoString = header.getBuildedString();
        }
    }
}