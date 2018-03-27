package pt.isel.ls.view.command;

import pt.isel.ls.model.Session;
import pt.isel.ls.model.Ticket;

import java.util.LinkedList;

public class GetCinemaIDTheaterIDSessionIDTicketIDView extends CommandView {
    private int cinemaId, theaterId, sessionId;
    private LinkedList<Ticket> tickets = new LinkedList<>();

    public GetCinemaIDTheaterIDSessionIDTicketIDView(int cinemaId, int theaterId, int sessionId) {
        this.cinemaId = cinemaId;
        this.theaterId = theaterId;
        this.sessionId = sessionId;
    }

    public void add(Ticket ticket) {
        tickets.add(ticket);
    }

    @Override
    public void printAllInfo() {
        System.out.println("TODO: implement");
    }

    @Override
    public LinkedList getList() {
        return tickets;
    }

    @Override
    public Ticket getSingle() {
        return tickets.getFirst();
    }
}