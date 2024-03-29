package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;

import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;


public class GetMoviesView extends JsonView {

    public GetMoviesView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        String[] tableColumns = {"id", "title", "year", "duration"};
        json.addArray(tableColumns, tableAux(tableColumns));
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
