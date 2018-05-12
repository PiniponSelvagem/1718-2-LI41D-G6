package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;

import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;


public class GetMoviesView extends CommandView {

    public GetMoviesView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Movies");
        String[] tableColumns = {"ID", "Title", "Year", "Duration"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "title", "year", "duration"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Movie> movies = (LinkedList<Movie>) data.getData(D_MOVIES);
        String[][] tableData  = new String[movies.size()][columnNames.length];
        Movie movie;
        for (int y=0; y<movies.size(); ++y) {
            movie = movies.get(y);
            tableData[y][0] = String.valueOf(movie.getId());
            tableData[y][1] = String.valueOf(movie.getTitle());
            tableData[y][2] = String.valueOf(movie.getYear());
            tableData[y][3] = String.valueOf(movie.getDuration());
        }
        return tableData;
    }
}
