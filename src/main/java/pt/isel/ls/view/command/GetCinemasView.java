package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMAS;

public class GetCinemasView extends CommandView {

    public GetCinemasView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Cinemas");
        String[] tableColumns = {"ID", "Name", "City"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        String[] tableColumns = {"Name", "City"};
        Writable[] th = new Writable[tableColumns.length];
        for (int i=0; i<tableColumns.length; ++i) {
            th[i] = th(text(tableColumns[i]));
        }

        LinkedList<Cinema> cinemas = (LinkedList<Cinema>) data.getData(D_CINEMAS);
        Writable[][] td = new Writable[cinemas.size()][tableColumns.length];
        Writable[] td_array = new Writable[cinemas.size()+1];
        td_array[0] = tr(th);
        Cinema cinema;
        for (int y=0; y<cinemas.size(); ++y) {
            cinema = cinemas.get(y);
            td[y][0] = td(
                    a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+cinema.getId(), cinema.getName())
            );
            td[y][1] = td(text(cinema.getCity()));
            td_array[y+1] = tr(td[y]);
        }

        header = new HtmlPage("Cinemas",
                h3(a(DIR_SEPARATOR.toString(), "Main page")),
                h1(text("Cinemas")),
                //h2(text("h2")),
                //h3(text("h3")),
                //li(text("test")),
                //text("text"),
                //a("/cinemas/1", "cinemas 1"),
                //textInput("textInput"),
                //ul(text("u1")),
                //label("to", "text"),
                //form("method", "/cinemas/1", text("test")),
                table(td_array)
        );

        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "name", "city"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Cinema> cinemas = (LinkedList<Cinema>) data.getData(D_CINEMAS);
        String[][] tableData  = new String[cinemas.size()][columnNames.length];
        Cinema cinema;
        for (int y=0; y<cinemas.size(); ++y) {
            cinema = cinemas.get(y);
            tableData[y][0] = String.valueOf(cinema.getId());
            tableData[y][1] = cinema.getName();
            tableData[y][2] = cinema.getCity();
        }
        return tableData;
    }
}