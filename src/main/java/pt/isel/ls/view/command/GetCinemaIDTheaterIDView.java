package pt.isel.ls.view.command;

import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.common.commands.GetCinemaIDSessionID;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.utils.HtmlViewCommon;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterIDView extends CommandView {

    public GetCinemaIDTheaterIDView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        Theater theater = (Theater) data.getData(D_THEATER);
        header.addDetailed("Theater (ID: " + theater.getId()+") [Cinema ID: " + theater.getCinemaID()+"]",
                new String[]{"Name", "Rows", "Seats per row", "Total seats"},
                new String[]{theater.getName(),
                        String.valueOf(theater.getRows()),
                        String.valueOf(theater.getSeatsPerRow()),
                        String.valueOf(theater.getSeats())}
        );

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header){
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
        Theater theater = (Theater) data.getData(D_THEATER);
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);

        String[] tableColumns = new String[]{"Date"};
        Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);

        Writable[][] td = new Writable[sessions.size()][tableColumns.length];
        Writable[] td_array = new Writable[sessions.size()+1];
        td_array[0] = tr(th);

        String hyperLink = new GetCinemaIDSessionID().getPath()
                .replace(CINEMA_ID_FULL.toString(), "%d")
                .replace(SESSION_ID_FULL.toString(), "%d"); //get path and make it ready to add IDs
        Session s;
        for (int j = 0; j < sessions.size(); j++) {
            s = sessions.get(j);
            td[j][0] = td(a(String.format(hyperLink, cinema.getId(), s.getId()), s.getDateTime()));
            td_array[j+1] = tr(td[j]);
        }

        hyperLink = new GetCinemaID().getPath()
                .replace(CINEMA_ID_FULL.toString(), "%d"); //get path and make it ready to add ID
        header = new HtmlPage("Theater " + theater.getName(),
                h3(a(String.format(hyperLink, cinema.getId()), "Cinema: "+cinema.getName())),
                h1(text("Theater " + theater.getName())),
                h2(text("Sessions: ")),
                table(td_array)
        );
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        Theater theater = (Theater) data.getData(D_THEATER);
        header.addObject(
                new String[]{"name", "rows", "seats", "total_seats"},
                new String[]{theater.getName(),
                        String.valueOf(theater.getRows()),
                        String.valueOf(theater.getSeatsPerRow()),
                        String.valueOf(theater.getSeats())}
        );

        return header.getBuildedString();
    }
}