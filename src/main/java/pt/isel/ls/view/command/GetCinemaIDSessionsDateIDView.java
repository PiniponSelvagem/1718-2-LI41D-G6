package pt.isel.ls.view.command;

import org.joda.time.DateTime;
import pt.isel.ls.core.common.commands.GetCinemaIDSessionsToday;
import pt.isel.ls.core.common.commands.GetCinemaIDTheaterID;
import pt.isel.ls.core.common.commands.GetCinemaIDTheaterIDSessionIDTicketID;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.core.utils.writable.Writable;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.common.headers.Html.a;
import static pt.isel.ls.core.common.headers.Html.h3;
import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID_FULL;
import static pt.isel.ls.core.strings.CommandEnum.THEATER_ID_FULL;
import static pt.isel.ls.core.strings.ExceptionEnum.DATE_INVALID_FORMAT;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_AVAILABLE_SEATS;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMA;

public class GetCinemaIDSessionsDateIDView extends CommandView {
    private int cinemaId;
    private Date date;

    public GetCinemaIDSessionsDateIDView(DataContainer data, int cinemaId, Date date) {
        this.data = data;
        this.cinemaId = cinemaId;
        this.date = date;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Sessions (CinemaID: "+cinemaId+") [Date: "+date+"]");
        String[] tableColumns = {"ID", "Starting time", "Movie Title", "Duration", "Theater name", "Available seats"};
        header.addTable(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        LinkedList<Integer> availableSeats = (LinkedList<Integer>)data.getData(D_AVAILABLE_SEATS);
        LinkedList<Session> sessions = (LinkedList<Session>)data.getData(D_SESSIONS);
        String[] tableColumns = {"Date", "Theater", "Movie", "Number Of Seats", "Available Seats"};
        Writable[][] td = new Writable[2][tableColumns.length];
        Writable[] td_array = new Writable[2];
        Session session;
        if(sessions.size()>0) {
            td = new Writable[sessions.size()+1][tableColumns.length];
            td_array = new Writable[sessions.size()+1];
            for (int i = 0; i < tableColumns.length; i++) td[0][i] = td(text(tableColumns[i]));
            td_array[0] = tr(td[0]);
            for (int i = 1; i <= sessions.size(); ++i) {
                session = sessions.get(i-1);
                td[i][0] = td(a("" + DIR_SEPARATOR + CINEMAS + DIR_SEPARATOR + session.getTheater().getCinemaID() +
                                DIR_SEPARATOR + SESSIONS + DIR_SEPARATOR + session.getId(),
                        session.getDateTime()));
                td[i][1] = td(a("" + DIR_SEPARATOR + CINEMAS + DIR_SEPARATOR + session.getTheater().getCinemaID() + DIR_SEPARATOR + THEATERS
                                + DIR_SEPARATOR + session.getTheater().getId(),
                        session.getTheater().getName()));
                td[i][2] = td(a("" + DIR_SEPARATOR + MOVIES + DIR_SEPARATOR + session.getMovie().getId(),
                        session.getMovie().getTitle()));
                td[i][3] = td(text("" + session.getTheater().getAvailableSeats()));
                td[i][4] = td(text("" + availableSeats.get(i-1)));
                td_array[i] = tr(td[i]);
            }
        }
        else {
            for (int i = 0; i < tableColumns.length; i++) td[0][i] = td(text(tableColumns[i]));
            td_array[0] = tr(td[0]);
            for (int i = 0; i < tableColumns.length; i++) td[1][i] = td(text("N/A"));
            td_array[1] = tr(td[1]);
        }
        SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy"); //format 1
        SimpleDateFormat sdf3= new SimpleDateFormat("ddMMyyyy"); //format 3

        String tomorrow= sdf3.format(new Date(this.date.getTime()+24*60*60*1000));
        String yesterday= sdf3.format(new Date(this.date.getTime()-24*60*60*1000));

        header = new HtmlPage("Sessions This Day: " + sdf1.format(this.date),
                table(td_array),
                h2(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+data.getData(D_CINEMA)
                        +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+DATE+DIR_SEPARATOR+yesterday,"Yesterday's Sessions")),
                h2(a(""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+data.getData(D_CINEMA)
                        +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+DATE+DIR_SEPARATOR+tomorrow,"Tomorrow's Sessions"))
        );
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        String[] tableColumns = {"id", "start_time", "movie_title", "duration", "theater_name", "available_seats"};
        header.addArray(tableColumns, tableAux(tableColumns));
        return header.getBuildedString();
    }

    private String[][] tableAux(String[] columnNames) {
        LinkedList<Session> sessions = (LinkedList<Session>) data.getData(D_SESSIONS);
        String[][] tableData  = new String[sessions.size()][columnNames.length];
        Session session;
        for (int y=0; y<sessions.size(); ++y) {
            session = sessions.get(y);
            tableData[y][0] = String.valueOf(session.getId());
            tableData[y][1] = session.getDateTime();
            tableData[y][2] = String.valueOf(session.getMovie().getTitle());
            tableData[y][3] = String.valueOf(session.getMovie().getDuration());
            tableData[y][4] = String.valueOf(session.getTheater().getName());
            tableData[y][5] = String.valueOf(session.getTheater().getAvailableSeats());
        }
        return tableData;
    }
}