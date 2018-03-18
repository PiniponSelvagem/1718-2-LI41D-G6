package pt.isel.ls.view.command;

import pt.isel.ls.model.Theater;

public class GetCinemaIDTheatersIDView extends CommandView {
    private Theater theater;

    public GetCinemaIDTheatersIDView(Theater theater) {
        this.theater = theater;
    }

    @Override
    public void printAllInfo() {
        System.out.println("Theater (ID: " + theater.getId()+") [Cinema ID: " + theater.getCinemaID()+"]");
        System.out.println("  > Name: " + theater.getName());
        System.out.println("  > Rows: " + theater.getRows());
        System.out.println("  > Seats per row: " + theater.getSeatsPerRow());
        System.out.println("  > Available seats: " + theater.getAvailableSeats());
    }

    @Override
    public Theater getSingle() {
        return theater;
    }
}