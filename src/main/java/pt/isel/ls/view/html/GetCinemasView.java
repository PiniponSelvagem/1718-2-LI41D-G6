package pt.isel.ls.view.html;

import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.common.commands.GetCinemas;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.view.html.utils.HtmlViewCommon;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.common.headers.html.HtmlElem.submit;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMAS;

public class GetCinemasView extends HtmlView {

    public GetCinemasView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        String[] tableColumns = {"Name", "City"};
        Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);

        LinkedList<Cinema> cinemas = (LinkedList<Cinema>) data.getData(D_CINEMAS);
        Writable[][] td = new Writable[cinemas.size()][tableColumns.length];
        Writable[] td_array = new Writable[cinemas.size()+1];
        td_array[0] = tr(th);
        Cinema cinema;
        String hyperLink = new GetCinemaID().getPath()
                .replace(CINEMA_ID_FULL.toString(), "%s"); //get path and make it ready to add ID
        for (int y=0; y<cinemas.size(); ++y) {
            cinema = cinemas.get(y);
            td[y][0] = td(
                    a(String.format(hyperLink, cinema.getId()), cinema.getName())
            );
            td[y][1] = td(text(cinema.getCity()));
            td_array[y+1] = tr(td[y]);
        }

        return new HtmlPage("Cinemas",
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
    }

    @Override
    public HttpStatusCode getCode() {
        return HttpStatusCode.OK;
    }
}