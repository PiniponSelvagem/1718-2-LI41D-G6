package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsDateIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.DATE_INVALID_FORMAT;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessionsDateID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+DATE+DIR_SEPARATOR+DATE_ID_FULL;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        DataContainer data=new DataContainer(cmdBuilder.getHeader());
        LocalDate localDate;
        String str = cmdBuilder.getId(DATE_ID.toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        //SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter3 = new SimpleDateFormat("ddMMyyyy");
        try {
            localDate = LocalDate.parse(str, formatter);
        } catch(DateTimeParseException e){
            return new InfoNotFoundView(data, DATE_INVALID_FORMAT.toString());
        }
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM CINEMA " +
                "WHERE cid=?"
        );

        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return new InfoNotFoundView(data);
        }
        else {
            data.add(D_CINEMA, new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }

        stmt = connection.prepareStatement(
                "SELECT s.sid, m.Title, m.Duration, t.Theater_Name, t.SeatsAvailable, s.Date, t.tid, m.mid," +
                        " s.SeatsAvailable FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "WHERE t.cid=? AND (CAST(s.Date AS DATE))=?"
        );

        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        stmt.setString(2, localDate.toString());
        rs = stmt.executeQuery();


        int sid, seats, cid = Integer.parseInt(cmdBuilder.getId(CINEMA_ID.toString())), tid, mid, duration, availableSeats;
        Timestamp date = null;
        String theaterName, title;

        LinkedList<Session> sessions = new LinkedList<>();
        while(rs.next()) {
            sid = rs.getInt(1);
            title = rs.getString(2);
            duration = rs.getInt(3);
            theaterName = rs.getString(4);
            seats = rs.getInt(5);
            date = rs.getTimestamp(6);
            tid = rs.getInt(7);
            mid = rs.getInt(8);
            availableSeats = rs.getInt(9);

            sessions.add(
                    new Session(sid, availableSeats, date,
                            new Movie(mid, title, NA, duration),
                            new Theater(tid, theaterName, NA, NA, seats, cid),
                            cid
                    )
            );
        }

        if (date==null) {
            try {
                date=new Timestamp(formatter3.parse(str).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        data.add(D_SESSIONS, sessions);
        return new GetCinemaIDSessionsDateIDView(data, cid, date);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}