package pt.isel.ls.view.json;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetMovieIDSessionsDateIDView extends JsonView {
    private int movieId;
    private Date date;

    public GetMovieIDSessionsDateIDView(DataContainer data) {
        super(data);
        this.movieId = (Integer) data.getData(D_MID);
        this.date = (Date) data.getData(D_DATE);
    }

    @Override
    protected void createJson() {
        String[] tableColumns = {"id", "cinema_id", "theater_name", "available_seats", "start_time"};
        json.addArray(tableColumns, tableAux(tableColumns));
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
        HashMap<Integer, Theater> theaters = (HashMap<Integer, Theater>) data.getData(D_THEATERS);
        String[][] tableData  = new String[sessions.size()][columnNames.length];
        Session session;
        for (int y=0; y<sessions.size(); ++y) {
            session = sessions.get(y);
            tableData[y][0] = String.valueOf(session.getId());
            tableData[y][1] = String.valueOf(session.getTheaterID());
            tableData[y][2] = theaters.get(session.getTheaterID()).getName();
            tableData[y][3] = String.valueOf(session.getAvailableSeats());
            tableData[y][4] = session.getDateTime();
        }
        return tableData;
    }
}