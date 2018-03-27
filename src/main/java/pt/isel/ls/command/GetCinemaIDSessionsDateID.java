package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsDateIDView;
import pt.isel.ls.view.command.GetCinemaIDSessionsView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.CINEMA_ID;

public class GetCinemaIDSessionsDateID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        //TODO:
        /*
           GET /cinemas/{cid}/sessions/date/{d} - return a list with the sessions in cinema cid in the day of the year d.
        */

        ResultSet rs = null; //= stmt.executeQuery();
        Date date = null; //TODO: add date here

        GetCinemaIDSessionsDateIDView sessionsView = new GetCinemaIDSessionsDateIDView(
                Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))),
                date
        );
        int id, mid, tid, availableSeats, rows, seatsRow, cid, year, duration;
        Date dateSession;
        String theaterName, title;

        while(rs.next()){
            id = rs.getInt(1);
            dateSession = rs.getDate(2);
            mid = rs.getInt(3);
            tid = rs.getInt(4);
            availableSeats = rs.getInt(6);
            rows = rs.getInt(7);
            seatsRow = rs.getInt(8);
            theaterName = rs.getString(9);
            cid = rs.getInt(10);
            title = rs.getString(12);
            year = rs.getInt(13);
            duration = rs.getInt(14);

            sessionsView.add(
                    new Session(id, dateSession,
                            new Movie(mid, title, year, duration),
                            new Theater(tid, theaterName, rows, seatsRow, availableSeats, cid),
                            cid
                    )
            );
        }

        return sessionsView;
    }
}
