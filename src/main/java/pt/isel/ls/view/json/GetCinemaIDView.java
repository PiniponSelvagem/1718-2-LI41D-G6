package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDView extends JsonView {

    public GetCinemaIDView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
        if (cinema!=null)
            json.addObject(
                    new String[]{"name", "city"},
                    new String[]{cinema.getName(), cinema.getCity()}
            );
        else
            setInfoNotFound();
    }
}