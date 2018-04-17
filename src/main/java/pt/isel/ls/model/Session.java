package pt.isel.ls.model;

import java.sql.Timestamp;

public class Session {
    private int id, cid;
    private Timestamp date;
    private Movie movie;
    private Theater theater;


    public Session(int id, Timestamp date, Movie movie, Theater theater, int cid) {
        this.id = id;
        this.date = date;
        this.movie = movie;
        this.theater = theater;
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public Timestamp getDate() {
        return date;
    }

    public Movie getMovie() {
        return movie;
    }

    public Theater getTheater() {
        return theater;
    }

    public int getCinemaID() {
        return cid;
    }
}
