package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;

import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATERS;


public class GetCinemaIDTheatersView extends JsonView {

    public GetCinemaIDTheatersView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        String[] tableColumns = {"id", "theater_name", "rows", "seats", "total_seats"};
        json.addArray(tableColumns, tableAux(tableColumns));
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