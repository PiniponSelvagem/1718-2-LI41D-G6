package pt.isel.ls.model;


import java.sql.Timestamp;

public class Session {
    private final int availableSeats;
    private final String dateTime, date, time, id, mid, cid, tid;

    public Session(String id, int availableSeats, Timestamp dateTime, String mid, String cid, String tid) {
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

    public String getId() {
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

    public String getMovieID() {
        return mid;
    }
    public String getCinemaID() {
        return cid;
    }
    public String getTheaterID() {
        return tid;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
}
