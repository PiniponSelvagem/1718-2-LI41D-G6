package pt.isel.ls.view.command;

import pt.isel.ls.core.utils.DataContainer;

import java.util.Date;

public class GetMovieIDSessionsDateIDView extends CommandView {
    private int movieId;
    private Date date;

    public GetMovieIDSessionsDateIDView(DataContainer data, int movieId, Date date) {
        this.data = data;
        this.movieId = movieId;
        this.date = date;
    }

    @Override
    public void printAllInfo() {
        System.out.println("TODO: implement");
    }
}