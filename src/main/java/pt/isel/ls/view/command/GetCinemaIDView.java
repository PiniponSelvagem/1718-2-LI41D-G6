package pt.isel.ls.view.command;

import pt.isel.ls.core.common.commands.GetCinemaIDSessionsToday;
import pt.isel.ls.core.common.commands.GetCinemaIDTheatersID;
import pt.isel.ls.core.common.commands.GetMovieID;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.utils.HtmlViewCommon;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

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
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
        LinkedList<Theater> theaters = (LinkedList<Theater>) data.getData(D_THEATERS);
        LinkedList<Movie> movies = (LinkedList<Movie>) data.getData(D_MOVIES);
        String[] tableColumns = {"Name"};
        Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);

        Writable[][] td;
        Writable[] td_array = new Writable[theaters.size()+1];
        td_array[0] = tr(th);
        Writable[] li_array = new Writable[theaters.size()];
        Theater theater;
        String hyperLink = new GetCinemaIDTheatersID().getPath()
                .replace(CINEMA_ID_FULL.toString(), "%d")
                .replace(THEATER_ID_FULL.toString(), "%d"); //get path and make it ready to add IDs
        for (int y=0; y<theaters.size(); ++y) {
            theater = theaters.get(y);
            li_array[y] = li(a(String.format(hyperLink, cinema.getId(), theater.getId()),
                    theater.getName()));
        }
        tableColumns = new String[]{"Title", "Release Year", "Duration"};
        td = new Writable[movies.size()][tableColumns.length];
        th = new Writable[tableColumns.length];
        for (int i = 0; i < tableColumns.length; i++) {
            th[i] = th(text(tableColumns[i]));
        }

        td_array = new Writable[movies.size()+1];
        td_array[0] = tr(th);
        Movie m;
        hyperLink = new GetMovieID().getPath()
                .replace(MOVIE_ID_FULL.toString(), "%d"); //get path and make it ready to add ID
        for (int j = 0; j < movies.size(); j++) {
            m = movies.get(j);
            td[j][0] = td(a(String.format(hyperLink, m.getId()), m.getTitle()));
            td[j][1] = td(text(Integer.toString(m.getYear())));
            td[j][2] = td(text(Integer.toString(m.getDuration())));
            td_array[j+1] = tr(td[j]);
        }

        hyperLink = new GetCinemaIDSessionsToday().getPath()
                .replace(CINEMA_ID_FULL.toString(), "%d"); //get path and make it ready to add ID
        header = new HtmlPage("Cinema " + cinema.getName(),
                h3(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR, "Cinemas")),
                h1(text("Cinema " + cinema.getName())),
                li(text("City: "+ cinema.getCity())),
                h2(text("Theaters: ")),
                multipleElems(li_array),
                h2(text("Movies: ")),
                table(td_array),
                h3(a(String.format(hyperLink, cinema.getId()), "Sessions today"))
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