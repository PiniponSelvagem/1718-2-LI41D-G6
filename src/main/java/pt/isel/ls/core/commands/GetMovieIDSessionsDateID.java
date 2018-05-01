package pt.isel.ls.core.commands;

import pt.isel.ls.Main;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDSessionIDTicketsAvailableView;
import pt.isel.ls.view.command.GetMovieIDSessionsDateIDView;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetMovieIDSessionsDateID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException, CommandException {

        LocalDate localDate;
        String str = cmdBuilder.getId(String.valueOf(DATE_ID));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        localDate = LocalDate.parse(str, formatter);
        Date date1 = Date.valueOf(localDate);


        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        int sid = 0, mid, tid, availableSeats, rows, seatsRow, cid, year, duration;
        Timestamp date = null;
        String theaterName, title;

        if (cmdBuilder.hasParameter(String.valueOf(CITY))) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT s.sid, s.Date, m.mid, t.tid, SEATS.seats, t.Rows, t.Seats, t.Theater_Name, c.cid, m.Title, m.Release_Year, m.Duration " +
                            "FROM MOVIE AS m INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                            "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                            "INNER JOIN SEATS ON SEATS.sid = s.sid " +
                            "INNER JOIN CINEMA AS c ON t.cid=c.cid AND c.City=? " +
                            "WHERE m.mid=? AND (CAST(s.Date AS DATE))=?"
            );
            stmt.setString(1, cmdBuilder.getParameter(String.valueOf(CITY)));
            stmt.setString(2, cmdBuilder.getId(String.valueOf(MOVIE_ID)));
            stmt.setDate(3, date1);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sid = rs.getInt(1);
                date = rs.getTimestamp(2);
                mid = rs.getInt(3);
                tid = rs.getInt(4);
                availableSeats = rs.getInt(5);
                rows = rs.getInt(6);
                seatsRow = rs.getInt(7);
                theaterName = rs.getString(8);
                cid = rs.getInt(9);
                title = rs.getString(10);
                year = rs.getInt(11);
                duration = rs.getInt(12);

                data.add(new Session(sid, date, new Movie(mid, title, year, duration),
                        new Theater(tid, theaterName, rows, seatsRow, availableSeats, cid), cid));
            }

        } else if (cmdBuilder.hasParameter(String.valueOf(CINEMA_ID))) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT s.sid, s.Date,m.mid,t.tid,SEATS.seats,t.Rows, t.Seats, t.Theater_Name,c.cid, m.Title, m.Release_Year ,m.Duration " +
                            "FROM MOVIE AS m INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                            "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                            "INNER JOIN SEATS ON SEATS.sid=s.sid " +
                            "INNER JOIN CINEMA AS c ON t.cid=c.cid AND c.cid = ? " +
                            "WHERE (CAST(s.Date AS DATE))=? AND m.mid=?"
            );
            stmt.setString(1, cmdBuilder.getParameter(String.valueOf(CINEMA_ID)));
            stmt.setDate(2, date1);
            stmt.setString(3, cmdBuilder.getId(String.valueOf(MOVIE_ID)));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sid = rs.getInt(1);
                date = rs.getTimestamp(2);
                mid = rs.getInt(3);
                tid = rs.getInt(4);
                availableSeats = rs.getInt(5);
                rows = rs.getInt(6);
                seatsRow = rs.getInt(7);
                theaterName = rs.getString(8);
                cid = rs.getInt(9);
                title = rs.getString(10);
                year = rs.getInt(11);
                duration = rs.getInt(12);

                data.add(new Session(sid, date, new Movie(mid, title, year, duration),
                        new Theater(tid, theaterName, rows, seatsRow, availableSeats, cid), cid));
            }

        } else if (cmdBuilder.hasParameter(String.valueOf(AVAILABLE))) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT s.sid, s.Date,m.mid,t.tid,SEATS.seats,t.Rows, t.Seats, t.Theater_Name,c.cid, m.Title, m.Release_Year ,m.Duration " +
                            "FROM MOVIE AS m INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                            "INNER JOIN SEATS ON SEATS.sid=s.sid " +
                            "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                            "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                            "WHERE (CAST(s.Date AS DATE))=? AND m.mid=? AND SEATS.seats>=?"
            );
            stmt.setString(3, cmdBuilder.getParameter(String.valueOf(AVAILABLE)));
            stmt.setDate(1, date1);
            stmt.setString(2, cmdBuilder.getId(String.valueOf(MOVIE_ID)));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sid = rs.getInt(1);
                date = rs.getTimestamp(2);
                mid = rs.getInt(3);
                tid = rs.getInt(4);
                availableSeats = rs.getInt(5);
                rows = rs.getInt(6);
                seatsRow = rs.getInt(7);
                theaterName = rs.getString(8);
                cid = rs.getInt(9);
                title = rs.getString(10);
                year = rs.getInt(11);
                duration = rs.getInt(12);


                /*GetCinemaIDTheaterIDSessionIDTicketsAvailableView view = (GetCinemaIDTheaterIDSessionIDTicketsAvailableView) Main.executeBuildedCommand(connection, new CommandBuilder(new String[]{"GET", "/cinemas/" + cid +"/theaters/"+tid+"/sessions/" + sid+"/tickets/available"}, new CommandUtils()));
                dataAux = view.getData();*/

                /*if(Integer.compare((int)dataAux.getData(0) , Integer.parseInt(cmdBuilder.getParameter(String.valueOf(AVAILABLE)))) >= 0)*/
                    data.add(new Session(sid, date, new Movie(mid, title, year, duration),
                        new Theater(tid, theaterName, rows, seatsRow, availableSeats, cid), cid));
            }

        } else {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT s.sid, s.Date,m.mid,t.tid,SEATS.seats,t.Rows, t.Seats, t.Theater_Name,c.cid, m.Title, m.Release_Year ,m.Duration " +
                            "FROM MOVIE AS m INNER JOIN CINEMA_SESSION AS s ON m.mid=s.mid " +
                            "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                            "INNER JOIN SEATS ON SEATS.sid=s.sid " +
                            "INNER JOIN CINEMA AS c ON t.cid=c.cid " +
                            "WHERE (CAST(s.Date AS DATE))=? AND m.mid=?"
            );
            stmt.setDate(1,date1);
            stmt.setString(2, cmdBuilder.getId(String.valueOf(MOVIE_ID)));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sid = rs.getInt(1);
                date = rs.getTimestamp(2);
                mid = rs.getInt(3);
                tid = rs.getInt(4);
                availableSeats = rs.getInt(5);
                rows = rs.getInt(6);
                seatsRow = rs.getInt(7);
                theaterName = rs.getString(8);
                cid = rs.getInt(9);
                title = rs.getString(10);
                year = rs.getInt(11);
                duration = rs.getInt(12);

                data.add(new Session(sid, date, new Movie(mid, title, year, duration),
                        new Theater(tid, theaterName, rows, seatsRow, availableSeats, cid), cid));
            }
        }
        return new GetMovieIDSessionsDateIDView(
                data,
                Integer.parseInt(cmdBuilder.getId(String.valueOf(MOVIE_ID))),
                date
        );
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
