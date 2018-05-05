package pt.isel.ls.view.command;


import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;

public class GetCinemaIDSessionIDView extends CommandView {

    public GetCinemaIDSessionIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected void allInfo() {
        Header header = data.getHeader();

        if (header != null) {
            Session session = (Session) data.getData(0);
            header.addObject("Cinema "+session.getCinemaID()+" - Session "+session.getId(),
                    new String[]{"Date", "Title", "Duration", "Theater name", "Available seats"},
                    new String[]{session.getDateTime(),
                            session.getMovie().getTitle(),
                            String.valueOf(session.getMovie().getDuration()),
                            session.getTheater().getName(),
                            String.valueOf(session.getTheater().getAvailableSeats())}
            );

            header.close();
            header.writeToFile();

            infoString = header.getBuildedString();
        }
    }
}