package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;


public class GetCinemaIDTheaterIDSessionsView extends CommandView {
    private int cinemaId, theaterId;

    public GetCinemaIDTheaterIDSessionsView(DataContainer data, int cinemaId, int theaterId) {
        this.data = data;
        this.cinemaId = cinemaId;
        this.theaterId = theaterId;
    }

    @Override
    public void printAllInfo() {
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Sessions for theater: (TheaterID: "+theaterId+") [CinemaID: "+cinemaId+"]");

            String[][] tableData  = new String[data.size()][5];
            String[] tableColumns = {"ID", "Date", "Title", "Duration", "Available seats"};

            Session session;
            for (int y=0; y<data.size(); ++y) {
                session = (Session) data.getData(y);
                tableData[y][0] = String.valueOf(session.getId());
                tableData[y][1] = session.getDate().toString();
                tableData[y][2] = String.valueOf(session.getMovie().getTitle());
                tableData[y][3] = String.valueOf(session.getMovie().getDuration());
                tableData[y][4] = String.valueOf(session.getTheater().getAvailableSeats());
            }
            header.addTable(tableColumns, tableData);

            header.close();
            header.writeToFile();

            System.out.println(header.getBuildedString());
        }
    }
}