package pt.isel.ls.model;

public class Theater {
    private int id, rows, seatsRow, availableSeats, cid;
    private String name;


    public Theater(int id, String name, int rows, int seatsRow, int availableSeats, int cid) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.seatsRow = seatsRow;
        this.availableSeats = availableSeats;
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

    public int getAvailableSeats() {
        return availableSeats;
    }

    public int getCinemaID() {
        return cid;
    }
}
