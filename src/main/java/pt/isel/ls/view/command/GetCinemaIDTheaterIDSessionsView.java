package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;

import java.util.LinkedList;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SESSIONS;


public class GetCinemaIDTheaterIDSessionsView extends CommandView {
    private int cinemaId, theaterId;

    public GetCinemaIDTheaterIDSessionsView(DataContainer data, int cinemaId, int theaterId) {
        this.data = data;
        this.cinemaId = cinemaId;
        this.theaterId = theaterId;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Sessions for theater: (TheaterID: "+theaterId+") [CinemaID: "+cinemaId+"]");
        String[] tableColumns = {"ID", "Date", "Title", "Duration", "Available seats"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "date", "title", "duration", "available_seats"};
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
            tableData[y][4] = String.valueOf(session.getTheater().getAvailableSeats());
        }
        return tableData;
    }
}