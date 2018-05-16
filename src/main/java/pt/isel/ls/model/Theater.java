package pt.isel.ls.model;

public class Theater {
    private final int id, rows, seatsRow, seats, cid;
    private final String name;


    public Theater(int id, String name, int rows, int seatsRow, int seats, int cid) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.seatsRow = seatsRow;
        this.seats = seats;
        this.cid = cid;
    }

    public int getId() {
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

    public int getCinemaID() {
        return cid;
    }
}
