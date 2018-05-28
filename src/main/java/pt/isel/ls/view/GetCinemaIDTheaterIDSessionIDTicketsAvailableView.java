package pt.isel.ls.view;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.commands.db_queries.SessionsSQL.NA_AVAILABLESEATS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_AVAILABLE_SEATS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SID;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailableView extends CommandView{
    private int sessionId;

    public GetCinemaIDTheaterIDSessionIDTicketsAvailableView(DataContainer data) {
        super(data);
        this.sessionId = (Integer) data.getData(D_SID);
    }

    @Override
    protected String toPlain(Plain header) {
        int availableSeats = (Integer) data.getData(D_AVAILABLE_SEATS);
        if (availableSeats != NA_AVAILABLESEATS)
            header.addTitle("For session "+ sessionId +" there are "+ availableSeats +" available seats.");
        else
            new InfoNotFoundView(data).toPlain(header);

        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        int availableSeats = (Integer) data.getData(D_AVAILABLE_SEATS);
        if (availableSeats != NA_AVAILABLESEATS)
            header.addObject(
                    new String[]{"available_seats"},
                    new String[]{String.valueOf(availableSeats)});
        else
            new InfoNotFoundView(data).toJson(header);

        return header.getBuildedString();
    }
}
