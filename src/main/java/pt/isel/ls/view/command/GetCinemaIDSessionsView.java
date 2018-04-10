package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;


public class GetCinemaIDSessionsView extends CommandView {
    private int cinemaId;

    public GetCinemaIDSessionsView(DataContainer data, int cinemaId) {
        this.data = data;
        this.cinemaId = cinemaId;
    }

    @Override
    public void printAllInfo() {
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Sessions (CinemaID: "+cinemaId+")");

            String[][] tableData  = new String[data.size()][6];
            String[] tableColumns = {"ID", "Date", "Title", "Duration", "Theater name", "Available seats"};

            Session session;
            for (int y=0; y<data.size(); ++y) {
                session = (Session) data.getData(y);
                tableData[y][0] = String.valueOf(session.getId());
                tableData[y][1] = session.getDate().toString();
                tableData[y][2] = String.valueOf(session.getMovie().getTitle());
                tableData[y][3] = String.valueOf(session.getMovie().getDuration());
                tableData[y][4] = String.valueOf(session.getTheater().getName());
                tableData[y][5] = String.valueOf(session.getTheater().getAvailableSeats());
            }
            header.addTable(tableColumns, tableData);

            header.close();
            header.writeToFile();

            System.out.println(header.getBuildedString());
        }
    }
}