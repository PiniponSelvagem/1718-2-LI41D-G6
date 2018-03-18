package pt.isel.ls.view.command;

import pt.isel.ls.model.Theater;

import java.util.LinkedList;

public class GetCinemaIDTheatersView extends CommandView {
    private final int cinemaId;
    private LinkedList<Theater> theaters = new LinkedList<>();

    public GetCinemaIDTheatersView(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    public void add(Theater theater) {
        theaters.add(theater);
    }

    @Override
    public void printAllInfo() {
        System.out.println("Theaters (CinemaID: "+cinemaId+")");
        System.out.println("   ID   |    Theater Name    | Rows | Seats per row | Available seats ");
        System.out.println("--------+--------------------+------+---------------+-----------------");
        for (Theater theater : theaters) {
            System.out.println(String.format("%7d", theater.getId())
                    + " | " + String.format("%18s", theater.getName())
                    + " | " + String.format("%4s", theater.getRows())
                    + " | " + String.format("%13s", theater.getSeatsPerRow())
                    + " | " + String.format("%15s", theater.getAvailableSeats())
            );
        }
    }

    @Override
    public LinkedList getList() {
        return theaters;
    }

    @Override
    public Theater getSingle() {
        return theaters.getFirst();
    }
}