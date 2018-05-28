package pt.isel.ls.view;

import pt.isel.ls.apps.http_server.http.HttpStatusCode;
import pt.isel.ls.core.common.commands.GetCinemaID;
import pt.isel.ls.core.common.commands.GetCinemaIDSessionID;
import pt.isel.ls.core.common.commands.GetCinemaIDTheaterIDSessions;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.utils.HtmlViewCommon;

import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.common.headers.html_utils.HtmlElem.submit;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterIDView extends CommandView {

    public GetCinemaIDTheaterIDView(DataContainer data) {
        super(data);
    }

    @Override
    protected String toPlain(Plain header) {
        Theater theater = (Theater) data.getData(D_THEATER);
        if (theater!=null)
            header.addDetailed("Theater (ID: " + theater.getId()+") [Cinema ID: " + theater.getCinemaID()+"]",
                    new String[]{"Name", "Rows", "Seats per row", "Total seats"},
                    new String[]{theater.getName(),
                            String.valueOf(theater.getRows()),
                            String.valueOf(theater.getSeatsPerRow()),
                            String.valueOf(theater.getSeats())}
            );
        else
            new InfoNotFoundView(data).toPlain(header);

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(HtmlPage header){
        Cinema cinema = (Cinema) data.getData(D_CINEMA);
        Theater theater = (Theater) data.getData(D_THEATER);
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);

        if (cinema!=null && theater!=null) {
            String[] tableColumns = new String[]{"Date"};
            Writable[] th = HtmlViewCommon.fillTableHeader(tableColumns);

            Writable[][] td = new Writable[sessions.size()][tableColumns.length];
            Writable[] td_array = new Writable[sessions.size() + 1];
            td_array[0] = tr(th);

            String hyperLink = new GetCinemaIDSessionID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%d")
                    .replace(SESSION_ID_FULL.toString(), "%d"); //get path and make it ready to add IDs
            Session session;
            for (int j = 0; j < sessions.size(); j++) {
                session = sessions.get(j);
                td[j][0] = td(a(String.format(hyperLink, cinema.getId(), session.getId()), session.getDateTime()));
                td_array[j + 1] = tr(td[j]);
            }

            hyperLink = new GetCinemaID().getPath()
                    .replace(CINEMA_ID_FULL.toString(), "%d"); //get path and make it ready to add ID

            String hyperLink_post = new GetCinemaIDTheaterIDSessions().getPath()
                    .replace(CINEMA_ID_FULL.toString(), String.valueOf(cinema.getId()))
                    .replace(THEATER_ID_FULL.toString(), String.valueOf(theater.getId()));

            header.createPage(HttpStatusCode.OK, "Theater " + theater.getName(),
                    h3(a(String.format(hyperLink, cinema.getId()), "Cinema: " + cinema.getName())),
                    h1(text("Theater " + theater.getName())),
                    h2(text("Sessions: ")),
                    table(td_array),
                    breakLine(),
                    form(POST.toString(), hyperLink_post,
                            text("Movie ID:"), breakLine(),
                            textInput(MOVIE_ID.toString()), breakLine(),
                            text("Date Time:"), breakLine(),
                            textInput(DATE.toString()), breakLine(),
                            submit("Create")
                    )
            );
        }
        else
            return new InfoNotFoundView(data).toHtml(header);

        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        Theater theater = (Theater) data.getData(D_THEATER);
        if (theater!=null)
            header.addObject(
                    new String[]{"name", "rows", "seats", "total_seats"},
                    new String[]{theater.getName(),
                            String.valueOf(theater.getRows()),
                            String.valueOf(theater.getSeatsPerRow()),
                            String.valueOf(theater.getSeats())}
            );
        else
            new InfoNotFoundView(data).toJson(header);

        return header.getBuildedString();
    }
}