package pt.isel.ls.view.command;

import pt.isel.ls.model.Session;

import java.util.Date;
import java.util.LinkedList;

public class GetCinemaIDSessionsDateIDView extends CommandView {
    private int cinemaId;
    private Date date;
    private LinkedList<Session> sessions = new LinkedList<>();

    public GetCinemaIDSessionsDateIDView(int cinemaId, Date date) {
        this.cinemaId = cinemaId;
        this.date = date;
    }

    public void add(Session session) {
        sessions.add(session);
    }

    @Override
    public void printAllInfo() {
        System.out.println("Sessions (CinemaID: "+cinemaId+") [Date: "+date);
        System.out.println("   ID   |        Title       | Duration |    Theater Name    | Available seats ");
        System.out.println("--------+--------------------+----------+--------------------+-----------------");
        for (Session sessions : sessions) {
            System.out.println(String.format("%7d", sessions.getId())
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
    public Session getSingle() {
        return sessions.getFirst();
    }
}