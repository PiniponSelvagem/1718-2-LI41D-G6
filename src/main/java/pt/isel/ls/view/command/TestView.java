package pt.isel.ls.view.command;

import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;

public class TestView extends CommandView {

    public TestView(DataContainer data) {
        this.data = data;
    }

    @Override
    public void printAllInfo() {
        Header header = data.getHeader();

        if (header != null) {
            Cinema cinema = (Cinema) data.getData(0);
            header.addTitle("!!! THIS IS JUST A TESTING VIEW !!!");
            header.addObject("Cinema "+cinema.getId(),
                    new String[]{"Name", "City"},
                    new String[]{cinema.getName(), cinema.getCity()}
            );

            header.close();
            header.writeToFile();

            System.out.println(header.getBuildedString());
        }
    }
}
