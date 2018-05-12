package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;

import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SESSIONS;

public class GetCinemaIDSessionIDView extends CommandView {

    public GetCinemaIDSessionIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        Session session = (Session) data.getData(D_SESSIONS);
        header.addDetailed("Cinema "+session.getCinemaID()+" - Session "+session.getId(),
                new String[]{"Date", "Title", "Duration", "Theater name", "Available seats"},
                new String[]{session.getDateTime(),
                        session.getMovie().getTitle(),
                        String.valueOf(session.getMovie().getDuration()),
                        session.getTheater().getName(),
                        String.valueOf(session.getTheater().getAvailableSeats())}
        );

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        Session session = (Session) data.getData(D_SESSIONS);
        header.addObject(
                new String[]{"Date", "Title", "Duration", "Theater name", "Available seats"},
                new String[]{session.getDateTime(),
                        session.getMovie().getTitle(),
                        String.valueOf(session.getMovie().getDuration()),
                        session.getTheater().getName(),
                        String.valueOf(session.getTheater().getAvailableSeats())}
        );

        return header.getBuildedString();
    }
}