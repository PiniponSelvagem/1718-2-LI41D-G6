package pt.isel.ls.view.plain;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;

import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMAS;

public class GetCinemasView extends PlainView {

    public GetCinemasView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        plain.addTitle("Cinemas");
        String[] tableColumns = {"ID", "Name", "City"};
        plain.addTable(tableColumns, tableAux(tableColumns));
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Cinema> cinemas = (LinkedList<Cinema>) data.getData(D_CINEMAS);
        String[][] tableData  = new String[cinemas.size()][columnNames.length];
        Cinema cinema;

        for (int y=0; y<cinemas.size(); ++y) {
            cinema = cinemas.get(y);
            tableData[y][0] = String.valueOf(cinema.getId());
            tableData[y][1] = cinema.getName();
            tableData[y][2] = cinema.getCity();
        }
        return tableData;
    }
}