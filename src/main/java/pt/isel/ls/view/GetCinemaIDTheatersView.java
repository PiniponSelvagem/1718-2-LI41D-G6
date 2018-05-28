package pt.isel.ls.view;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;

import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATERS;


public class GetCinemaIDTheatersView extends CommandView {
    private final int cinemaId;

    public GetCinemaIDTheatersView(DataContainer data) {
        super(data);
        this.cinemaId = (Integer) data.getData(D_CID);
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Theaters (CinemaID: "+cinemaId+")");
        String[] tableColumns = {"ID", "Theater name", "Rows", "Seats per row", "Total seats"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "theater_name", "rows", "seats", "total_seats"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
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