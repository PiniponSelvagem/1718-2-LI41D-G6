package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.InfoNotFoundView;
import pt.isel.ls.view.command.PostView;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class PostCinemaIDTheaterIDSessions extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {

        //formato da data ao inserir como comando = dd/MM/yyyy+HH:mm:ss
        try{
            String ts = cmdBuilder.getParameter(String.valueOf(DATE_PARAM));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime newSessionDate;
            LocalDateTime lastSessionDate;
            newSessionDate = LocalDateTime.parse(ts, formatter);

            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT m.Duration FROM MOVIE AS m " +
                            "WHERE m.mid=?"
            );
            int duration = 0;
            boolean allow = true;
            stmt.setString(1, cmdBuilder.getParameter(String.valueOf(MOVIE_ID)));

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) duration = rs.getInt(1);

            stmt = connection.prepareStatement("select CINEMA_SESSION.Date AS Date, MOVIE.Duration as Duration from CINEMA_SESSION " +
                    "INNER JOIN MOVIE ON MOVIE.mid=CINEMA_SESSION.mid " +
                    "ORDER BY Date");
            rs = stmt.executeQuery();

            int newSessionTime;
            int lastSessionTime;
            LocalDate localDate = newSessionDate.toLocalDate();
            LocalTime localTime = newSessionDate.toLocalTime();

            while(rs.next()){
                lastSessionDate = rs.getTimestamp(1).toLocalDateTime();
                if(localDate.equals(lastSessionDate.toLocalDate())){
                    if(newSessionDate.isAfter(lastSessionDate)){
                        newSessionTime = localTime.getHour()*60 + localTime.getMinute();
                        lastSessionTime = lastSessionDate.getHour()*60 + lastSessionDate.getMinute();
                        int dif = newSessionTime - lastSessionTime;
                        if(!(dif >= rs.getInt(2)) || dif == 0) allow = false;
                    }
                    else if(newSessionDate.isBefore(lastSessionDate)){
                        newSessionTime = localTime.getHour()*60 + localTime.getMinute();
                        lastSessionTime = lastSessionDate.getHour()*60 + lastSessionDate.getMinute();
                        int dif = lastSessionTime - newSessionTime;
                        if(!(dif >= duration) || dif == 0) allow = false;
                    }
                    else allow = false;
                }
            }

            if(allow){
                stmt = connection.prepareStatement("INSERT INTO CINEMA_SESSION VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setTimestamp(1, Timestamp.valueOf(newSessionDate));
                stmt.setString(2, cmdBuilder.getParameter((String.valueOf(MOVIE_ID))));
                stmt.setString(3, cmdBuilder.getId(String.valueOf(THEATER_ID)));
                stmt.executeUpdate();

                rs = stmt.getGeneratedKeys();
                int id = 0;
                if (rs.next()) id = rs.getInt(1);
                return new PostView<>("Session: ", id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new InfoNotFoundView();
    }
}