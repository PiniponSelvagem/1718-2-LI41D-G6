package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterIDView extends PlainView {

    public GetCinemaIDTheaterIDView(DataContainer data) {
        super(data);
    }

    @Override
    protected void createPlain() {
        Theater theater = (Theater) data.getData(D_THEATER);
        if (theater!=null)
            plain.addDetailed("Theater (ID: " + theater.getId()+") [Cinema ID: " + theater.getCinemaID()+"]",
                    new String[]{"Name", "Rows", "Seats per row", "Total seats"},
                    new String[]{theater.getName(),
                            String.valueOf(theater.getRows()),
                            String.valueOf(theater.getSeatsPerRow()),
                            String.valueOf(theater.getSeats())}
            );
        else
            plain.addTitle(infoNotFound);
    }
}