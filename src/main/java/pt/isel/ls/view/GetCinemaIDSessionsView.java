package pt.isel.ls.view;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;

import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


public class GetCinemaIDSessionsView extends CommandView {
    private int cinemaId;

    public GetCinemaIDSessionsView(DataContainer data) {
        super(data);
        this.cinemaId = (Integer) data.getData(D_CID);
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Sessions (CinemaID: "+cinemaId+")");
        String[] tableColumns = {"ID", "Date", "Title", "Duration", "Theater name", "Available seats"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "date", "title", "duration", "theater_name", "available_seats"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
        HashMap<Integer, Movie> movies = (HashMap<Integer, Movie>) data.getData(D_MOVIES);
        HashMap<Integer, Theater> theaters = (HashMap<Integer, Theater>) data.getData(D_THEATERS);
        String[][] tableData = new String[sessions.size()][columnNames.length];
        Session session;
        for (int y=0; y<sessions.size(); ++y) {
            session = sessions.get(y);
            tableData[y][0] = String.valueOf(session.getId());
            tableData[y][1] = session.getDateTime();
            tableData[y][2] = movies.get(session.getMovieID()).getTitle();
            tableData[y][3] = String.valueOf(movies.get(session.getMovieID()).getDuration());
            tableData[y][4] = theaters.get(session.getTheaterID()).getName();
            tableData[y][5] = String.valueOf(session.getAvailableSeats());
        }
        return tableData;
    }
}