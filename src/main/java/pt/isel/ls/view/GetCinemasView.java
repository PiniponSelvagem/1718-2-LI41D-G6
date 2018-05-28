package pt.isel.ls.view;

import pt.isel.ls.apps.http_server.http.HttpStatusCode;
import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.common.commands.GetCinemas;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.view.utils.HtmlViewCommon;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.common.headers.html_utils.HtmlElem.submit;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMAS;

public class GetCinemasView extends CommandView {

    public GetCinemasView(DataContainer data) {
        super(data);
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Cinemas");
        String[] tableColumns = {"ID", "Name", "City"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(HtmlPage header) {
        String[] tableColumns = {"Name", "City"};
        Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);

        LinkedList<Cinema> cinemas = (LinkedList<Cinema>) data.getData(D_CINEMAS);
        Writable[][] td = new Writable[cinemas.size()][tableColumns.length];
        Writable[] td_array = new Writable[cinemas.size()+1];
        td_array[0] = tr(th);
        Cinema cinema;
        String hyperLink = new GetCinemaID().getPath()
                .replace(CINEMA_ID_FULL.toString(), "%d"); //get path and make it ready to add ID
        for (int y=0; y<cinemas.size(); ++y) {
            cinema = cinemas.get(y);
            td[y][0] = td(
                    a(String.format(hyperLink, cinema.getId()), cinema.getName())
            );
            td[y][1] = td(text(cinema.getCity()));
            td_array[y+1] = tr(td[y]);
        }

        header.createPage(HttpStatusCode.OK, "Cinemas",
                h3(a(DIR_SEPARATOR.toString(), "Main page")),
                h1(text("Cinemas")),
                table(td_array),
                breakLine(),
                form(POST.toString(), new GetCinemas().getPath(),
                        text("Name:"), breakLine(),
                        textInput(NAME.toString()), breakLine(),
                        text("City:"), breakLine(),
                        textInput(CITY.toString()), breakLine(),
                        submit("Create")
                )
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