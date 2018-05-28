package pt.isel.ls.view;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;

import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


public class GetCinemaIDTheaterIDSessionIDTicketsView extends CommandView {
    private int cinemaId, sessionId;

    public GetCinemaIDTheaterIDSessionIDTicketsView(DataContainer data) {
        super(data);
        this.cinemaId = (Integer) data.getData(D_CID);
        this.sessionId = (Integer) data.getData(D_SID);
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Tickets (CinemaID: "+cinemaId+") [SessionID: "+sessionId+"]");
        String[] tableColumns = {"ID", "Date", "Title", "Duration", "Theater name"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "date", "title", "duration", "theater_name"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Ticket> tickets = (LinkedList<Ticket>) data.getData(D_TICKETS);
        Session session = (Session) data.getData(D_SESSION);
        Movie movie = (Movie) data.getData(D_MOVIE);
        Theater theater = (Theater) data.getData(D_THEATER);
        String[][] tableData  = new String[tickets.size()][columnNames.length];
        Ticket ticket;
        for (int y=0; y<tickets.size(); ++y) {
            ticket = tickets.get(y);
            tableData[y][0] = String.valueOf(ticket.getId());
            tableData[y][1] = session.getDateTime();
            tableData[y][2] = movie.getTitle();
            tableData[y][3] = String.valueOf(movie.getDuration());
            tableData[y][4] = theater.getName();
        }
        return tableData;
    }
}