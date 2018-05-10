package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;

public class GetCinemaIDView extends CommandView {
    private String theaterName;
    private String movieTitle;
    private  String date;
    private int seatsAv;
    private int movieDur;
    private int movRelease;
    private int theaterCount;

    public GetCinemaIDView(DataContainer data, int theaterCount) {
        this.data = data;
        this.theaterCount = theaterCount;
    }

    @Override
    protected void allInfo() {
        Theater theater = (Theater) data.getData(1);
        Header header = data.getHeader();

        if (header != null) {
        Cinema cinema = (Cinema) data.getData(0);
        header.addObject("Cinema "+cinema.getId(),
                new String[]{"Name", "City"},
                new String[]{cinema.getName(), cinema.getCity()}
        );
        System.out.println("");
        header.addTitle("Theaters for Cinema " + cinema.getName() + ": (Theater Name: "+theater.getName()+")");

            String[] tableColumns = {"Theater Name"};
            String[][] tableData  = new String[theaterCount][tableColumns.length];

            for (int y=0; y<tableData.length; ++y) {
                tableData[y][0] = theater.getName();
            }
            header.addTable("Theater - Name:"+theater.getName(), tableColumns, tableData);
            infoString = header.getBuildedString();





            /*Cinema cinema = (Cinema) data.getData(0);
            header.addObject("Cinema "+cinema.getId(),
                    new String[]{"Name", "City"},
                    new String[]{cinema.getName(), cinema.getCity()}
            );

            header.close();
            header.writeToFile();
            */



        }
    }
}