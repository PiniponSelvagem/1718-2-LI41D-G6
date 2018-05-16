package pt.isel.ls.model;


import java.sql.Timestamp;

public class Session {
    private final int id, availableSeats, cid;
    private final String dateTime, date, time;
    private final Movie movie;
    private final Theater theater;


    public Session(int id, int availableSeats, Timestamp dateTime, Movie movie, Theater theater, int cid) {
        this.id = id;
        this.availableSeats = availableSeats;
        this.dateTime = dateTime.toLocalDateTime().toString().replace('T', ' ');
        this.date = dateTime.toLocalDateTime().toString().substring(
                0,
                dateTime.toLocalDateTime().toString().indexOf('T')
        );
        this.time = dateTime.toLocalDateTime().toString().substring(
                dateTime.toLocalDateTime().toString().indexOf('T')+1,
                dateTime.toLocalDateTime().toString().length()
        );
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
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getDateNoSeparators() {
        return date.replace("-", "");
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

    public int getAvailableSeats() {
        return availableSeats;
    }
}
