package pt.isel.ls.view.command;

import pt.isel.ls.model.Session;

import java.util.LinkedList;

public class GetCinemaIDSessionsView extends CommandView {
    private int cinemaId;
    private LinkedList<Session> sessions = new LinkedList<>();

    public GetCinemaIDSessionsView(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    public void add(Session session) {
        sessions.add(session);
    }

    @Override
    public void printAllInfo() {
        System.out.println("Sessions (CinemaID: "+cinemaId+")");
        System.out.println("   ID   |    Date    |        Title       | Duration |    Theater Name    | Available seats ");
        System.out.println("--------+------------+--------------------+----------+--------------------+-----------------");
        for (Session sessions : sessions) {
            System.out.println(String.format("%7d", sessions.getId())
                    + " | " + sessions.getDate()
                    + " | " + String.format("%18s", sessions.getMovie().getTitle())
                    + " | " + String.format("%8d", sessions.getMovie().getDuration())
                    + " | " + String.format("%18s", sessions.getTheater().getName())
                    + " | " + String.format("%14d", sessions.getTheater().getAvailableSeats())
            );
        }
    }

    @Override
    public LinkedList getList() {
        return sessions;
    }

    @Override
    public Object getSingle() {
        return sessions.getFirst();
    }
}