package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_AVAILABLE_SEATS;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailableView extends CommandView{
    private int sessionId;
    public GetCinemaIDTheaterIDSessionIDTicketsAvailableView(DataContainer data, int sessionId) {
        this.data = data;
        this.sessionId=sessionId;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("For session "+ sessionId +" there are "+ data.getData(D_AVAILABLE_SEATS) +" available seats.");
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        header.addObject(
                new String[]{"available_seats"},
                new String[]{data.getData(D_AVAILABLE_SEATS).toString()});

        return header.getBuildedString();
    }
}
