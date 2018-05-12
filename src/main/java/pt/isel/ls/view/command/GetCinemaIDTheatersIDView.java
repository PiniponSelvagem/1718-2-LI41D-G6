package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATER;

public class GetCinemaIDTheatersIDView extends CommandView {

    public GetCinemaIDTheatersIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        Theater theater = (Theater) data.getData(D_THEATER);
        header.addDetailed("Theater (ID: " + theater.getId()+") [Cinema ID: " + theater.getCinemaID()+"]",
                new String[]{"Name", "Rows", "Seats per row", "Total seats"},
                new String[]{theater.getName(),
                        String.valueOf(theater.getRows()),
                        String.valueOf(theater.getSeatsPerRow()),
                        String.valueOf(theater.getAvailableSeats())}
        );

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        Theater theater = (Theater) data.getData(D_THEATER);
        header.addObject(
                new String[]{"name", "rows", "seats", "total_seats"},
                new String[]{theater.getName(),
                        String.valueOf(theater.getRows()),
                        String.valueOf(theater.getSeatsPerRow()),
                        String.valueOf(theater.getAvailableSeats())}
        );

        return header.getBuildedString();
    }
}