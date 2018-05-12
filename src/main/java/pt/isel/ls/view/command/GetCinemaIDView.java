package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlElem;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMA;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMAS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATERS;

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


        //int id = 1; //just for the example, this should be replaced by something like: cinema.getId();
        /*header = new HtmlPage("Cinema",
            h1(text("Cinema "+cinema.getName()));
            h2(text("Cinema theaters:")),
                li(text("Sample text 1")),
                li(text("Sample text 2")),
            //h3(text("Header 3")),
            li(text("Sample text 1")),
            li(text("Sample text 2")),
            a("/movies/1", "Go to movie 1"),
            ul(text("u1 - 1")),
            ul(text("u1 - 2")),
            label("label", "label text"),
            textInput("textInput"),     //for some reason, this stays after the HtmlElem thats above it, idk why atm
            form("GET", "/cinemas/1", text("form text")) //dont know what this does atm, but probably will be used for POST
            //table(td_array) //check GetCinemasView for a better example
        );*/
        //---------------------------""-------------------------------------"-"---------
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
        LinkedList<Theater> theaters = (LinkedList<Theater>) data.getData(D_THEATERS);
        String[] tableColumns = {"Name"};
        Writable[] th = new Writable[tableColumns.length];
        for (int i=0; i<tableColumns.length; ++i) {
            th[i] = th(text(tableColumns[i]));
        }

        //LinkedList<Cinema> cinemas = (LinkedList<Cinema>) data.getData(D_CINEMAS);
        Writable[][] td = new Writable[theaters.size()][tableColumns.length];
        Writable[] td_array = new Writable[theaters.size()+1];
        td_array[0] = tr(th);
        Writable[] li_array = new Writable[theaters.size()];
        Theater theater;
        for (int y=0; y<theaters.size(); ++y) {
            theater = theaters.get(y);
            /*td[y][0] = td(
                    a(""+DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+theater.getId(), theater.getName())
            );
            td_array[y+1] = tr(td[y]);*/
            li_array[y] = li(a(""+DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+theater.getId(), theater.getName()));
        }

        /*Writable[] li_array = new Writable[2];
        li_array[0] = li(text("test1"));
        li_array[1] = li(text("test2"));*/

        header = new HtmlPage("Cinema" + cinema.getName(),
                h3(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR, "Cinemas")),
                h3(a(""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR, "Movies")),
                h1(text("Cinema" + cinema.getName())),
                li(text("City: "+ cinema.getCity())),
                h2(text("Theaters: ")),
                multipleElems(li_array),
                h3(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+cinema.getId()+DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+DATE+DIR_SEPARATOR+TODAY, "Sessions today"))
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