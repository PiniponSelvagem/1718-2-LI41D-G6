package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;

import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;


public class GetCinemaIDTheaterIDSessionsView extends JsonView {

    public GetCinemaIDTheaterIDSessionsView(DataContainer data) {
        super(data);
    }

    @Override
    public void createView() {
        String[] tableColumns = {"id", "date", "title", "duration", "available_seats"};
        json.addArray(tableColumns, tableAux(tableColumns));
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
        HashMap<String, Movie> movies = (HashMap<String, Movie>) data.getData(D_MOVIES);
        String[][] tableData  = new String[sessions.size()][columnNames.length];
        Session session;
        for (int y=0; y<sessions.size(); ++y) {
            session = sessions.get(y);
            tableData[y][0] = String.valueOf(session.getId());
            tableData[y][1] = session.getDateTime();
            tableData[y][2] = movies.get(session.getId()).getTitle();
            tableData[y][3] = String.valueOf(movies.get(session.getId()).getDuration());
            tableData[y][4] = String.valueOf(session.getAvailableSeats());
        }
        return tableData;
    }
}