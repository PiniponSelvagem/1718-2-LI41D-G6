package pt.isel.ls.view.command;


import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;


public class GetMoviesView extends CommandView {

    int cid;
    String tname;

    public GetMoviesView(DataContainer data) {
        this.data = data;
    }
    public GetMoviesView(DataContainer data, int cid, String tname) {
        this.data = data;
        this.cid = cid;
        this.tname = tname;
    }

    @Override
    public void printAllInfo() {
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Movies");

            String[] tableColumns = {"ID", "Title", "Year", "Duration", "CinemaID", "Theater Name"};
            String[][] tableData  = new String[data.size()][tableColumns.length];

            Movie movie;
            for (int y=0; y<data.size(); ++y) {
                movie = (Movie) data.getData(y);
                tableData[y][0] = String.valueOf(movie.getId());
                tableData[y][1] = String.valueOf(movie.getTitle());
                tableData[y][2] = String.valueOf(movie.getYear());
                tableData[y][3] = String.valueOf(movie.getDuration());
                tableData[y][4] = String.valueOf(cid);
                tableData[y][5] = String.valueOf(tname);
            }
            header.addTable("Movie", tableColumns, tableData);

            header.close();
            header.writeToFile();

            System.out.println(header.getBuildedString());
        }
    }
}
