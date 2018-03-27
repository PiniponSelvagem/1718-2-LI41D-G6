package pt.isel.ls.view.command;

import pt.isel.ls.model.Session;

import java.util.Date;
import java.util.LinkedList;

public class GetMovieIDSessionsDateIDView extends CommandView {
    private int movieId;
    private Date date;
    private LinkedList<Session> sessions = new LinkedList<>();

    public GetMovieIDSessionsDateIDView(int movieId, Date date) {
        this.movieId = movieId;
        this.date = date;
    }

    public void add(Session session) {
        sessions.add(session);
    }

    @Override
    public void printAllInfo() {
        System.out.println("TODO: implement");
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