package pt.isel.ls.view.command;


import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;


public class GetMoviesView extends CommandView {

    public GetMoviesView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected void allInfo() {
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Movies");

            String[] tableColumns = {"ID", "Title", "Year", "Duration"};
            String[][] tableData  = new String[data.size()][tableColumns.length];

            Movie movie;
            for (int y=0; y<data.size(); ++y) {
                movie = (Movie) data.getData(y);
                tableData[y][0] = String.valueOf(movie.getId());
                tableData[y][1] = String.valueOf(movie.getTitle());
                tableData[y][2] = String.valueOf(movie.getYear());
                tableData[y][3] = String.valueOf(movie.getDuration());
            }
            header.addTable("Movie", tableColumns, tableData);

            header.close();
            header.writeToFile();

            infoString = header.getBuildedString();
        }
    }
}
