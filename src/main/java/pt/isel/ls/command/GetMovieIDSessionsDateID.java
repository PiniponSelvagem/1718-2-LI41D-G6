package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsDateIDView;
import pt.isel.ls.view.command.GetMovieIDSessionsDateIDView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.command.strings.CommandEnum.MOVIE_ID;

public class GetMovieIDSessionsDateID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        //TODO:
        /*
            GET /movies/{mid}/sessions/date/{d} - returns a list with the sessions for the movie identified by mid in the day of the year d given one of the following optional parameters:
                city - the city name;
                cid - the cinema identifier;
                available- the minimum number of available seats.
        */

        ResultSet rs = null; //= stmt.executeQuery();
        Date date = null; //TODO: add date here

        GetMovieIDSessionsDateIDView sessionsView = new GetMovieIDSessionsDateIDView(
                Integer.parseInt(cmdBuilder.getId(String.valueOf(MOVIE_ID))),
                date
        );
        int id, mid, tid, availableSeats, rows, seatsRow, cid, year, duration;
        Date dateSession;
        String theaterName, title;

        while(rs.next()){
            //TODO: something like GetCinemaIDSessions
        }

        return sessionsView;
    }
}
