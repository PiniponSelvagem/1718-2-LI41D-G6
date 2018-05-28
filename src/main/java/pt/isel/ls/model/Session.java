package pt.isel.ls.model;


import java.sql.Timestamp;

public class Session {
    private final int id, availableSeats, mid, cid, tid;
    private final String dateTime, date, time;

    public Session(int id, int availableSeats, Timestamp dateTime, int mid, int cid, int tid) {
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
        this.mid = mid;
        this.cid = cid;
        this.tid = tid;
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

    public int getMovieID() {
        return mid;
    }
    public int getCinemaID() {
        return cid;
    }
    public int getTheaterID() {
        return tid;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
}
