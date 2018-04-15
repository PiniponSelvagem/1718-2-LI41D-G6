package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Ticket;


public class GetCinemaIDTheaterIDSessionIDTicketsView extends CommandView {
    private int sessionId;

    public GetCinemaIDTheaterIDSessionIDTicketsView(DataContainer data ,int sessionId ) {
        this.data = data;
        this.sessionId=sessionId;
    }

    @Override
    public void printAllInfo() {
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Tickets (CinemaID: "+sessionId+")");

            String[] tableColumns = {"ID", "Date", "Title", "Duration", "Theater name", "Seat"};
            String[][] tableData  = new String[data.size()][tableColumns.length];

            Ticket ticket;
            for (int y=0; y<data.size(); ++y) {
                ticket = (Ticket) data.getData(y);
                tableData[y][0] = String.valueOf(ticket.getId());
                tableData[y][1] = ticket.getSession().getDate().toString();
                tableData[y][2] = String.valueOf(ticket.getSession().getMovie().getTitle());
                tableData[y][3] = String.valueOf(ticket.getSession().getMovie().getDuration());
                tableData[y][4] = String.valueOf(ticket.getSession().getTheater().getName());
                tableData[y][5] = String.valueOf(ticket.getRow()+""+ticket.getSeat());
            }
            header.addTable(tableColumns, tableData);

            header.close();
            header.writeToFile();

            System.out.println(header.getBuildedString());
        }
        System.out.println("TODO: implement");
    }
}