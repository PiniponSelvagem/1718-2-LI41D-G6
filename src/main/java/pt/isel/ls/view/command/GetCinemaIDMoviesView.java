package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;

import java.util.Date;

public class GetCinemaIDMoviesView extends CommandView {
    private int cinemaId;
    private Date date;

    public GetCinemaIDMoviesView(DataContainer data, int cinemaId,  Date date) {
        this.data = data;
        this.cinemaId = cinemaId;
        this.date = date;
    }

    @Override
    public void printAllInfo() {
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Cinema: "+cinemaId+" [Date: "+date+"]");

            String[] tableColumns = {"ID", "Title", "Duration"};
            String[][] tableData  = new String[data.size()][tableColumns.length];

            Movie movie;
            for (int y=0; y<data.size(); ++y) {
                movie = (Movie) data.getData(y);
                tableData[y][0] = String.valueOf(movie.getId());
                tableData[y][1] = String.valueOf(movie.getTitle());
                tableData[y][2] = String.valueOf(movie.getDuration());
            }
            header.addTable("Cinema cid:"+cinemaId, tableColumns, tableData);

            header.close();
            header.writeToFile();

            System.out.println(header.getBuildedString());
        }
    }
}