package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDView extends PlainView {

    public GetCinemaIDView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
        if (cinema!=null)
            plain.addDetailed("Cinema "+cinema.getId(),
                    new String[]{"Name", "City"},
                    new String[]{cinema.getName(), cinema.getCity()}
            );
        else
            plain.addTitle(infoNotFound);
    }
}