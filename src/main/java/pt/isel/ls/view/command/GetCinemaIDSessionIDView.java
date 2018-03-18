package pt.isel.ls.view.command;


import pt.isel.ls.model.Session;

public class GetCinemaIDSessionIDView extends CommandView {
    private Session session;

    public GetCinemaIDSessionIDView(Session session) {
        this.session = session;
    }

    @Override
    public void printAllInfo() {
        System.out.println("Cinema (ID: " + session.getCinemaID()+")");
        System.out.println("  > Date: " + session.getDate());
        System.out.println("  > Title: " + session.getMovie().getTitle());
        System.out.println("  > Duration: " + session.getMovie().getDuration());
        System.out.println("  > Theater Name: " + session.getTheater().getName());
        System.out.println("  > Available seats: " + session.getTheater().getAvailableSeats());
    }

    @Override
    public Session getSingle() {
        return session;
    }
}