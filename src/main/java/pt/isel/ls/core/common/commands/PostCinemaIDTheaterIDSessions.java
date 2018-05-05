package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.DATE_INVALID_FORMAT;

public class PostCinemaIDTheaterIDSessions extends Command {

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX+DIR_SEPARATOR+SESSIONS;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {

        SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //format 1
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //format 2
        Date date, newDate, event;
        Timestamp timestamp;
        PreparedStatement stmt;
        ResultSet rs;
        String check;
        boolean flag=true;
        int duration,eventDuration=0;

        try {
            check=cmdBuilder.getParameter(DATE_PARAM.toString())+":00";
            sdf1.setLenient(false);
            event = sdf1.parse(check.trim());
        } catch (ParseException e) {
            try {
                check=cmdBuilder.getParameter(DATE_PARAM.toString())+":00";
                sdf2.setLenient(false);
                event = sdf2.parse(check.trim());
            } catch (ParseException ex) {
                throw new CommandException(DATE_INVALID_FORMAT);
            }
        }

        stmt = connection.prepareStatement(
                "SELECT m.Duration FROM MOVIE AS m "+
                        "WHERE m.mid=?"
        );
        stmt.setString(1, cmdBuilder.getParameter(MOVIE_ID.toString()));
        rs = stmt.executeQuery();

        if(rs.next())eventDuration=rs.getInt(1);
        Date newEvent=new Date(event.getTime() + eventDuration * 60000);

        stmt = connection.prepareStatement(
                "SELECT s.Date, m.Duration FROM CINEMA_SESSION AS s " +
                        "INNER JOIN MOVIE AS m ON m.mid=s.mid "+
                        "WHERE s.tid=?"
        );
        stmt.setString(1, cmdBuilder.getId(THEATER_ID.toString()));
        stmt.execute();
        rs = stmt.executeQuery();

        while(rs.next()){ //Check if DATE is already in use
            timestamp = rs.getTimestamp(1);
            if (timestamp != null)
                date = new Date(timestamp.getTime());
            else {date=rs.getDate(1);}
            duration=rs.getInt(2);
            newDate =new Date(date.getTime() + (duration * 60000));
            if((date.before(event) && newDate.after(event)) || (event.before(date) && newEvent.after(date)) || date.equals(event) || newDate.equals(newEvent) || date.equals(newEvent) || event.equals(newDate)) {
                flag=false;
                break;
            }
        }

        if (flag) { //If DATE free, then POST
            stmt = connection.prepareStatement("INSERT INTO CINEMA_SESSION VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setTimestamp(1,new Timestamp(event.getTime()));
            stmt.setString(2, cmdBuilder.getParameter(MOVIE_ID.toString()));
            stmt.setString(3, cmdBuilder.getId(THEATER_ID.toString()));
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            int id = 0;
            if (rs.next()) id = rs.getInt(1);

            int seats = 0;
            stmt = connection.prepareStatement("SELECT THEATER.SeatsAvailable FROM THEATER WHERE THEATER.tid=?");
            stmt.setString(1, cmdBuilder.getId(THEATER_ID.toString()));
            rs = stmt.executeQuery();
            if(rs.next()) seats = rs.getInt(1);

            stmt = connection.prepareStatement("INSERT INTO SEATS VALUES(?,?,?)");
            stmt.setInt(1, seats);
            stmt.setString(2, cmdBuilder.getId(THEATER_ID.toString()));
            stmt.setInt(3, id);

            stmt.executeUpdate();

            return new PostView<>("Session: ", id);
        }
        else {
            return new PostView<>("Session: ", "NOT POSTED!");
        }
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
