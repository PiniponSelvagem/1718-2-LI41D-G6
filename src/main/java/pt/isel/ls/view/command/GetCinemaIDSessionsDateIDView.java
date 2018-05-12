package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;

import java.util.Date;
import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SESSIONS;

public class GetCinemaIDSessionsDateIDView extends CommandView {
    private int cinemaId;
    private Date date;

    public GetCinemaIDSessionsDateIDView(DataContainer data, int cinemaId, Date date) {
        this.data = data;
        this.cinemaId = cinemaId;
        this.date = date;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Sessions (CinemaID: "+cinemaId+") [Date: "+date+"]");
        String[] tableColumns = {"ID", "Starting time", "Movie Title", "Duration", "Theater name", "Available seats"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "start_time", "movie_title", "duration", "theater_name", "available_seats"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
        String[][] tableData  = new String[sessions.size()][columnNames.length];
        Session session;
        for (int y=0; y<sessions.size(); ++y) {
            session = sessions.get(y);
            tableData[y][0] = String.valueOf(session.getId());
            tableData[y][1] = session.getDateTime();
            tableData[y][2] = String.valueOf(session.getMovie().getTitle());
            tableData[y][3] = String.valueOf(session.getMovie().getDuration());
            tableData[y][4] = String.valueOf(session.getTheater().getName());
            tableData[y][5] = String.valueOf(session.getTheater().getAvailableSeats());
        }
        return tableData;
    }
}