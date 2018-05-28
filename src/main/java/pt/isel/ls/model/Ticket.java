package pt.isel.ls.model;

public class Ticket {
    private final char row;
    private final int seat, sid;
    private final String id;


    public Ticket(char row, int seat, int sid) {
        this.row = row;
        this.seat = seat;
        this.id = row+""+seat;
        this.sid = sid;
    }

    public String getId() {
        return id;
    }

    public char getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    public int getSessionID() {
        return sid;
    }
}
