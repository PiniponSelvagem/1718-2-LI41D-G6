package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.commands.db_queries.SessionsSQL.NA_AVAILABLESEATS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_AVAILABLE_SEATS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SID;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailableView extends PlainView{
    private String sessionId;

    public GetCinemaIDTheaterIDSessionIDTicketsAvailableView(DataContainer data) {
        super(data);
        this.sessionId = (String) data.getData(D_SID);
    }

    @Override
    protected void createPlain() {
        int availableSeats = (Integer) data.getData(D_AVAILABLE_SEATS);
        if (availableSeats != NA_AVAILABLESEATS)
            plain.addTitle("For session "+ sessionId +" there are "+ availableSeats +" available seats.");
        else
            plain.addTitle(infoNotFound);
    }

}
