package pt.isel.ls.view.command;

import pt.isel.ls.model.Movie;

public class GetMovieIDView extends CommandView {
    private Movie movie;

    public GetMovieIDView(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void printAllInfo() {
        System.out.println("Movie (ID: " + movie.getId()+")");
        System.out.println("  > Title: " + movie.getTitle());
        System.out.println("  > Year: " + movie.getYear());
        System.out.println("  > Duration: " + movie.getDuration());
    }

    @Override
    public Movie getSingle() {
        return movie;
    }
}
