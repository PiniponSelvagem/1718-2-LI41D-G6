package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;

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
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Sessions (MovieID: "+movieId+") [Date: "+date+"]");

            String[] tableColumns = {"ID", "Cinema ID", "Theater name", "Available seats", "Starting time"};
            String[][] tableData  = new String[data.size()][tableColumns.length];

            Session session;
            for (int y=0; y<data.size(); ++y) {
                session = (Session) data.getData(y);
                tableData[y][0] = String.valueOf(session.getId());
                tableData[y][1] = String.valueOf(session.getTheater().getCinemaID());
                tableData[y][2] = String.valueOf(session.getTheater().getName());
                tableData[y][3] = String.valueOf(session.getTheater().getAvailableSeats());
                tableData[y][4] = session.getDateTime();
            }
            header.addTable("Sessions - mid:"+movieId+" date:"+date, tableColumns, tableData);

            header.close();
            header.writeToFile();

            System.out.println(header.getBuildedString());
        }
    }
}