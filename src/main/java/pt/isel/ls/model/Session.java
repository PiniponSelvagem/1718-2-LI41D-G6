package pt.isel.ls.model;

import java.util.Date;

public class Session {
    private int id, cid;
    private Date date;
    private Movie movie;
    private Theater theater;


    public Session(int id, Date date, Movie movie, Theater theater, int cid) {
        this.id = id;
        this.date = date;
        this.movie = movie;
        this.theater = theater;
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
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
