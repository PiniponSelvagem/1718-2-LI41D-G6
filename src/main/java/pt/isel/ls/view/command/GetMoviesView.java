package pt.isel.ls.view.command;

import pt.isel.ls.model.Movie;

import java.util.LinkedList;

public class GetMoviesView extends CommandView {
    private LinkedList<Movie> movies = new LinkedList<>();

    public void add(Movie movie) {
       movies.add(movie);
    }

    @Override
    public void printAllInfo() {
        System.out.println("Movies:");
        System.out.println("   ID   |        Title       | Year | Duration ");
        System.out.println("--------+--------------------+------+----------");
        for (Movie cinema : movies) {
            System.out.println(String.format("%7d", cinema.getId())
                    + " | " + String.format("%18s", cinema.getTitle())
                    + " | " + String.format("%4d", cinema.getYear())
                    + " | " + String.format("%8d", cinema.getDuration())
            );
        }
    }

    @Override
    public LinkedList getList() {
        return movies;
    }

    @Override
    public Object getSingle() {
        return movies.getFirst();
    }
}
