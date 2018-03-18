package pt.isel.ls.view.command;

import pt.isel.ls.model.Session;

import java.util.LinkedList;

public class GetCinemaIDTheaterIDSessionsView extends CommandView {
    private int cinemaId, theaterId;
    private LinkedList<Session> sessions = new LinkedList<>();

    public GetCinemaIDTheaterIDSessionsView(int cinemaId, int theaterId) {
        this.cinemaId = cinemaId;
        this.theaterId = theaterId;
    }

    public void add(Session session) {
        sessions.add(session);
    }

    @Override
    public void printAllInfo() {
        System.out.println("Sessions for theater: (TheaterID: "+theaterId+") [CinemaID: "+cinemaId+"]");
        System.out.println("   ID   |    Date    |        Title       | Duration | Available seats ");
        System.out.println("--------+------------+--------------------+----------+-----------------");
        for (Session sessions : sessions) {
            System.out.println(String.format("%7d", sessions.getId())
                    + " | " + sessions.getDate()
                    + " | " + String.format("%18s", sessions.getMovie().getTitle())
                    + " | " + String.format("%8d", sessions.getMovie().getDuration())
                    + " | " + String.format("%14d", sessions.getTheater().getAvailableSeats())
            );
        }
    }

    @Override
    public LinkedList getList() {
        return sessions;
    }

    @Override
    public Session getSingle() {
        return sessions.getFirst();
    }
}