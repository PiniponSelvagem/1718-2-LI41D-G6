package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Ticket;

import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_TICKETS;


public class GetCinemaIDTheaterIDSessionIDTicketsView extends CommandView {
    private int cinemaId, sessionId;

    public GetCinemaIDTheaterIDSessionIDTicketsView(DataContainer data, int cinemaId, int sessionId ) {
        this.data = data;
        this.cinemaId = cinemaId;
        this.sessionId = sessionId;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Tickets (CinemaID: "+cinemaId+") [SessionID: "+sessionId+"]");
        String[] tableColumns = {"ID", "Date", "Title", "Duration", "Theater name"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "date", "title", "duration", "theater_name"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Ticket> tickets = (LinkedList<Ticket>) data.getData(D_TICKETS);
        String[][] tableData  = new String[tickets.size()][columnNames.length];
        Ticket ticket;
        for (int y=0; y<tickets.size(); ++y) {
            ticket = tickets.get(y);
            tableData[y][0] = String.valueOf(ticket.getId());
            tableData[y][1] = ticket.getSession().getDateTime();
            tableData[y][2] = String.valueOf(ticket.getSession().getMovie().getTitle());
            tableData[y][3] = String.valueOf(ticket.getSession().getMovie().getDuration());
            tableData[y][4] = String.valueOf(ticket.getSession().getTheater().getName());
        }
        return tableData;
    }
}