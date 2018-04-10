package pt.isel.ls.view.command;

import pt.isel.ls.core.utils.DataContainer;


public class GetCinemaIDTheaterIDSessionIDTicketIDView extends CommandView {
    private int cinemaId, theaterId, sessionId;

    public GetCinemaIDTheaterIDSessionIDTicketIDView(DataContainer data, int cinemaId, int theaterId, int sessionId) {
        this.data = data;
        this.cinemaId = cinemaId;
        this.theaterId = theaterId;
        this.sessionId = sessionId;
    }

    @Override
    public void printAllInfo() {
        System.out.println("TODO: implement");
    }
}