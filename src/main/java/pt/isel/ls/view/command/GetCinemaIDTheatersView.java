package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;


public class GetCinemaIDTheatersView extends CommandView {
    private final int cinemaId;

    public GetCinemaIDTheatersView(DataContainer data, int cinemaId) {
        this.data = data;
        this.cinemaId = cinemaId;
    }

    @Override
    protected void allInfo() {
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Theaters (CinemaID: "+cinemaId+")");

            String[] tableColumns = {"ID", "Theater name", "Rows", "Seats per row", "Total seats"};
            String[][] tableData  = new String[data.size()][tableColumns.length];

            Theater theater;
            for (int y=0; y<data.size(); ++y) {
                theater = (Theater) data.getData(y);
                tableData[y][0] = String.valueOf(theater.getId());
                tableData[y][1] = theater.getName();
                tableData[y][2] = String.valueOf(theater.getRows());
                tableData[y][3] = String.valueOf(theater.getSeatsPerRow());
                tableData[y][4] = String.valueOf(theater.getAvailableSeats());
            }
            header.addTable("Theaters - cid:"+cinemaId, tableColumns, tableData);

            header.close();
            header.writeToFile();

            infoString = header.getBuildedString();
        }
    }
}