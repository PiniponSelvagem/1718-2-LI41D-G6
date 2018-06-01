package pt.isel.ls.model;

public class Movie {
    private final int year, duration;
    private final String title, id;


    public Movie(String id, String title, int year, int duration) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getDuration() {
        return duration;
    }
}
