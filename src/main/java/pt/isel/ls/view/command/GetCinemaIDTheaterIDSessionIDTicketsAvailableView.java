package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;

public class GetCinemaIDTheaterIDSessionIDTicketsAvailableView extends CommandView{
    private int sessionId;
    public GetCinemaIDTheaterIDSessionIDTicketsAvailableView(DataContainer data, int sessionId) {
        this.data = data;
        this.sessionId=sessionId;
    }

    @Override
    public void printAllInfo() {

        Header header = data.getHeader();
        if (header != null) {
            header.addTitle("For session "+ sessionId +" there are "+ data.getData(0) +" available seats.");
            header.close();
            header.writeToFile();
            System.out.println(header.getBuildedString());
        }
    }
}
