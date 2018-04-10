package pt.isel.ls.view.command;


import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Session;

public class GetCinemaIDSessionIDView extends CommandView {

    public GetCinemaIDSessionIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    public void printAllInfo() {
        Header header = data.getHeader();

        if (header != null) {
            Session session = (Session) data.getData(0);
            header.addObject("Cinema "+session.getCinemaID()+" - Session "+session.getId(),
                    new String[]{"Date", "Title", "Duration", "Theater name", "Available seats"},
                    new String[]{String.valueOf(session.getDate()), session.getMovie().getTitle(),
                            String.valueOf(session.getMovie().getDuration()), session.getTheater().getName(),
                            String.valueOf(session.getTheater().getAvailableSeats())}
            );

            header.close();
            header.writeToFile();

            System.out.println(header.getBuildedString());
        }
    }
}