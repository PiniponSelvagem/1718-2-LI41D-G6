package pt.isel.ls.core.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDSessionsDateIDView;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static pt.isel.ls.core.strings.CommandEnum.CINEMA_ID;
import static pt.isel.ls.core.strings.CommandEnum.DATE_ID;

public class GetCinemaIDSessionsDateID extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        LocalDate localDate;
        String str = cmdBuilder.getId(String.valueOf(DATE_ID));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        localDate = LocalDate.parse(str, formatter);

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.sid, m.Title, m.Duration, t.Theater_Name, st.seats, s.Date, t.cid, t.tid, m.mid FROM CINEMA_SESSION AS s " +
                "INNER JOIN THEATER AS t ON t.tid=s.tid " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid " +
                "INNER JOIN SEATS AS st ON st.sid = s.sid " +
                "WHERE cid=? AND (CAST(s.Date AS DATE))=?"
        );

        stmt.setString(1, cmdBuilder.getId(String.valueOf(CINEMA_ID)));
        stmt.setString(2, localDate.toString());
        ResultSet rs = stmt.executeQuery();

        DataContainer data=new DataContainer(cmdBuilder.getHeader());
        int sid, availableSeats, cid, tid, mid, duration;
        Timestamp date=null;
        String theaterName, title;

        while(rs.next()){
            sid = rs.getInt(1);
            date = rs.getTimestamp(2);
            title = rs.getString(3);
            duration = rs.getInt(4);
            theaterName = rs.getString(5);
            availableSeats = rs.getInt(6);
            cid = rs.getInt(7);
            tid = rs.getInt(8);
            mid = rs.getInt(9);

            data.add(new Session(sid, date,
                        new Movie(mid, title, NA, duration),
                        new Theater(tid, theaterName, NA, NA, availableSeats, cid), cid)
            );
        }

        return new GetCinemaIDSessionsDateIDView(
                data,
                Integer.parseInt(cmdBuilder.getId(String.valueOf(CINEMA_ID))),
                date
        );
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}