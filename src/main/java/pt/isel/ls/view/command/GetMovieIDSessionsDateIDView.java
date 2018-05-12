package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;

import java.util.Date;
import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SESSIONS;

public class GetMovieIDSessionsDateIDView extends CommandView {
    private int movieId;
    private Date date;

    public GetMovieIDSessionsDateIDView(DataContainer data, int movieId, Date date) {
        this.data = data;
        this.movieId = movieId;
        this.date = date;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Sessions (MovieID: "+movieId+") [Date: "+date+"]");
        String[] tableColumns = {"ID", "Cinema ID", "Theater name", "Available seats", "Starting time"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "cinema_id", "theater_name", "available_seats", "start_time"};
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
            tableData[y][1] = String.valueOf(session.getTheater().getCinemaID());
            tableData[y][2] = String.valueOf(session.getTheater().getName());
            tableData[y][3] = String.valueOf(session.getTheater().getAvailableSeats());
            tableData[y][4] = session.getDateTime();
        }
        return tableData;
    }
}