package pt.isel.ls.view.command;

import pt.isel.ls.model.Cinema;

public class GetCinemaIDView extends CommandView {
    private Cinema cinema;

    public GetCinemaIDView(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void printAllInfo() {
        System.out.println("Cinema (ID: " + cinema.getId()+")");
        System.out.println("  > Name: " + cinema.getName());
        System.out.println("  > City: " + cinema.getCity());
    }

    @Override
    public Cinema getSingle() {
        return cinema;
    }
}