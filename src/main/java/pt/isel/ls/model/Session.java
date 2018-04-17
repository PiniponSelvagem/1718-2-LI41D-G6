package pt.isel.ls.model;


import java.sql.Timestamp;

public class Session {
    private int id, cid;
    private String dateTime;
    private Movie movie;
    private Theater theater;


    public Session(int id, Timestamp dateTime, Movie movie, Theater theater, int cid) {
        this.id = id;
        this.dateTime = dateTime.toLocalDateTime().toString().replace('T', ' ');
        this.movie = movie;
        this.theater = theater;
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
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
