package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Ticket;


public class GetCinemaIDTheaterIDSessionIDTicketsView extends CommandView {
    private int cinemaId, sessionId;

    public GetCinemaIDTheaterIDSessionIDTicketsView(DataContainer data, int cinemaId, int sessionId ) {
        this.data = data;
        this.cinemaId = cinemaId;
        this.sessionId = sessionId;
    }

    @Override
    protected void allInfo() {
        Header header = data.getHeader();

        if (header != null) {
            header.addTitle("Tickets (CinemaID: "+cinemaId+") [SessionID: "+sessionId+"]");

            String[] tableColumns = {"ID", "Date", "Title", "Duration", "Theater name"};
            String[][] tableData  = new String[data.size()][tableColumns.length];

            Ticket ticket;
            for (int y=0; y<data.size(); ++y) {
                ticket = (Ticket) data.getData(y);
                tableData[y][0] = String.valueOf(ticket.getId());
                tableData[y][1] = ticket.getSession().getDateTime();
                tableData[y][2] = String.valueOf(ticket.getSession().getMovie().getTitle());
                tableData[y][3] = String.valueOf(ticket.getSession().getMovie().getDuration());
                tableData[y][4] = String.valueOf(ticket.getSession().getTheater().getName());
            }
            header.addTable("Tickets - cid:"+cinemaId+" sid:"+sessionId, tableColumns, tableData);

            header.close();
            header.writeToFile();

            infoString = header.getBuildedString();
        }
    }
}