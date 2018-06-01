package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.commands.db_queries.SessionsSQL.NA_AVAILABLESEATS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_AVAILABLE_SEATS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SID;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailableView extends JsonView {
    private int sessionId;

    public GetCinemaIDTheaterIDSessionIDTicketsAvailableView(DataContainer data) {
        super(data);
        this.sessionId = (Integer) data.getData(D_SID);
    }

    @Override
    protected void createJson() {
        int availableSeats = (Integer) data.getData(D_AVAILABLE_SEATS);
        if (availableSeats != NA_AVAILABLESEATS)
            json.addObject(
                    new String[]{"available_seats"},
                    new String[]{String.valueOf(availableSeats)});
        else
            setInfoNotFound();
    }
}
