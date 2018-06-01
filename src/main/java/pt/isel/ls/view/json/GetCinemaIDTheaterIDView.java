package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterIDView extends JsonView {

    public GetCinemaIDTheaterIDView(DataContainer data) {
        super(data);
    }

    @Override
    protected void createJson() {
        Theater theater = (Theater) data.getData(D_THEATER);
        if (theater!=null)
            json.addObject(
                    new String[]{"name", "rows", "seats", "total_seats"},
                    new String[]{theater.getName(),
                            String.valueOf(theater.getRows()),
                            String.valueOf(theater.getSeatsPerRow()),
                            String.valueOf(theater.getSeats())}
            );
        else
            setInfoNotFound();
    }
}