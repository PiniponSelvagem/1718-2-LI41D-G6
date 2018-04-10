package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;


public class GetCinemasView extends CommandView {

    public GetCinemasView(DataContainer data) {
        this.data = data;
    }

    @Override
    public void printAllInfo() {
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Cinemas");

            String[][] tableData  = new String[data.size()][3];
            String[] tableColumns = {"ID", "Name", "City"};

            Cinema cinema;
            for (int y=0; y<data.size(); ++y) {
                cinema = (Cinema) data.getData(y);
                tableData[y][0] = String.valueOf(cinema.getId());
                tableData[y][1] = cinema.getName();
                tableData[y][2] = cinema.getCity();
            }
            header.addTable(tableColumns, tableData);

            header.close();
            header.writeToFile();

            System.out.println(header.getBuildedString());
        }
    }
}