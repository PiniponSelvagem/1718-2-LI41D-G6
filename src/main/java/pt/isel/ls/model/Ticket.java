package pt.isel.ls.model;

public class Ticket {
    private final char row;
    private final int seat;
    private final String id;
    private final Session session;


    public Ticket(char row, int seat, Session session) {
        this.row = row;
        this.seat = seat;
        this.id = row+""+seat;
        this.session = session;
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

    public Session getSession() {
        return session;
    }
}
