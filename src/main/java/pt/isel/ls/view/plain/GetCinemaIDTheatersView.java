package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;
import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATERS;


public class GetCinemaIDTheatersView extends PlainView {
    private final String cinemaId;

    public GetCinemaIDTheatersView(DataContainer data) {
        super(data);
        this.cinemaId = (String) data.getData(D_CID);
    }

    @Override
    public void createView() {
        plain.addTitle("Theaters (CinemaID: "+cinemaId+")");
        String[] tableColumns = {"ID", "Theater name", "Rows", "Seats per row", "Total seats"};
        plain.addTable(tableColumns, tableAux(tableColumns));
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Theater> theaters = (LinkedList<Theater>) data.getData(D_THEATERS);
        String[][] tableData = new String[theaters.size()][columnNames.length];
        Theater theater;
        for (int y=0; y<theaters.size(); ++y) {
            theater = theaters.get(y);
            tableData[y][0] = String.valueOf(theater.getId());
            tableData[y][1] = theater.getName();
            tableData[y][2] = String.valueOf(theater.getRows());
            tableData[y][3] = String.valueOf(theater.getSeatsPerRow());
            tableData[y][4] = String.valueOf(theater.getSeats());
        }
        return tableData;
    }
}