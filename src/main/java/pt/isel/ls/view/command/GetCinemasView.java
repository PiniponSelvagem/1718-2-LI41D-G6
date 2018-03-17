package pt.isel.ls.view.command;

import pt.isel.ls.model.Cinema;

import java.util.LinkedList;

public class GetCinemasView extends CommandView {
    private LinkedList<Cinema> cinemas = new LinkedList<>();

    public void add(Cinema cinema) {
        cinemas.add(cinema);
    }

    @Override
    public void printAllInfo() {
        System.out.println("Cinemas:");
        System.out.println("   ID   |        Name        |        City        ");
        System.out.println("--------+--------------------+--------------------");
        for (Cinema cinema : cinemas) {
            System.out.println(String.format("%7d", cinema.getId())
                    + " | " + String.format("%18s", cinema.getName())
                    + " | " + String.format("%18s", cinema.getCity())
            );
        }
    }

    @Override
    public LinkedList getList() {
        return cinemas;
    }

    @Override
    public Object getSingle() {
        return cinemas.getFirst();
    }
}