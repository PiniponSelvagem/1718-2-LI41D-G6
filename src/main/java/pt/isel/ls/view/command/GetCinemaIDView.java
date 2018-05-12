package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMA;

public class GetCinemaIDView extends CommandView {

    public GetCinemaIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
        header.addDetailed("Cinema "+cinema.getId(),
                new String[]{"Name", "City"},
                new String[]{cinema.getName(), cinema.getCity()}
        );

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        /*
        Theater theater = (Theater) data.getData(1);
        Cinema cinema = (Cinema) data.getData(0);
        plain.addObject("Cinema "+cinema.getId(),
                new String[]{"Name", "City"},
                new String[]{cinema.getName(), cinema.getCity()}
        );
        System.out.println("");
        plain.addTitle("Theaters for Cinema " + cinema.getName() + ": (Theater Name: "+theater.getName()+")");

        String[] tableColumns = {"Theater Name"};
        String[][] tableData  = new String[theaterCount][tableColumns.length];

        for (int y=0; y<tableData.length; ++y) {
            tableData[y][0] = theater.getName();
        }
        plain.addTable(tableColumns, tableData);
         */


        int id = 1; //just for the example, this should be replaced by something like: cinema.getId();
        header = new HtmlPage("Cinema",
            h1(text("Cinema "+id)),
            h2(text("Header 2")),
            h3(text("Header 3")),
            li(text("Sample text 1")),
            li(text("Sample text 2")),
            a("/movies/1", "Go to movie 1"),
            ul(text("u1 - 1")),
            ul(text("u1 - 2")),
            label("label", "label text"),
            textInput("textInput"),     //for some reason, this stays after the HtmlElem thats above it, idk why atm
            form("GET", "/cinemas/1", text("form text")) //dont know what this does atm, but probably will be used for POST
            //table(td_array) //check GetCinemasView for a better example
        );


        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
        header.addObject(
                new String[]{"name", "city"},
                new String[]{cinema.getName(), cinema.getCity()}
        );

        return header.getBuildedString();
    }
}