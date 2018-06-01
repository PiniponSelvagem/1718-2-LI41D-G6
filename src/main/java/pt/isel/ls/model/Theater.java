package pt.isel.ls.model;

public class Theater {
    private final int rows, seatsRow, seats;
    private final String name, id, cid;


    public Theater(String id, String name, int rows, int seatsRow, int seats, String cid) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.seatsRow = seatsRow;
        this.seats = seats;
        this.cid = cid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getSeatsPerRow() {
        return seatsRow;
    }

    public int getSeats() {
        return seats;
    }

    public String getCinemaID() {
        return cid;
    }
}
