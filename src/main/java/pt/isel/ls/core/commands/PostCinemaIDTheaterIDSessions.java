package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class PostCinemaIDTheaterIDSessions extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        //                  Comandos de teste
        //POST /movies title=Inception&duration=148&releaseYear=2010
        //POST /cinemas name=City+Campo+Pequeno&city=Lisboa
        //POST /cinemas/1/theaters name=Sala+VIP&seat=12&row=10
        //POST /cinemas/1/theaters/1/sessions mid=1&date=2018/11/24+21:30
        //POST /cinemas/1/theaters/1/sessions mid=1&date=24/11/2018+22:30 suposto dar not posted
        //POST /cinemas/1/theaters/1/sessions mid=1&date=24/11/2018+23:59
        //POST /cinemas/1/theaters/1/sessions mid=1&date=2018/11/24+23:59 suposto dar not posted

        SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //format 1
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //format 2
        //Calendar aux = Calendar.getInstance();
        Date date, newDate, event = null;
        Timestamp timestamp;
        PreparedStatement stmt;
        ResultSet rs;
        String check;
        boolean flag=true;
        int duration,eventDuration=0;

        try {
            check=cmdBuilder.getParameter((String.valueOf(DATE_PARAM)))+":00";
            sdf1.setLenient(false);
            event = sdf1.parse(check.trim());
        } catch (ParseException e) {
            try {
                check=cmdBuilder.getParameter((String.valueOf(DATE_PARAM)))+":00";
                sdf2.setLenient(false);
                event = sdf2.parse(check.trim());
            } catch (ParseException ex) {
                System.out.println("Please enter a valid date!");
            }
        }

        stmt = connection.prepareStatement(
                "SELECT m.Duration FROM MOVIE AS m "+
                        "WHERE m.mid=?"
        );
        stmt.setString(1, cmdBuilder.getParameter(String.valueOf(MOVIE_ID)));
        rs = stmt.executeQuery();

        if(rs.next())eventDuration=rs.getInt(1);
        Date newEvent=new Date(event.getTime() + eventDuration * 60000);

        stmt = connection.prepareStatement(
                "SELECT s.Date, m.Duration FROM CINEMA_SESSION AS s " +
                        "INNER JOIN MOVIE AS m ON m.mid=s.mid "+
                        "WHERE s.tid=?"
        );
        stmt.setString(1, cmdBuilder.getId(String.valueOf(THEATER_ID)));
        stmt.execute();
        rs = stmt.executeQuery(); //Todas as datas e durações de sessões

        while(rs.next()){ //verificar se a data da sessão está em uso
            timestamp = rs.getTimestamp(1);
            if (timestamp != null)
                date = new Date(timestamp.getTime());
            else {date=rs.getDate(1);}
            duration=rs.getInt(2);
            //aux.setTimeInMillis(date.getTime());
            newDate =new Date(date.getTime() + (duration * 60000));
            if((date.before(event) && newDate.after(event)) || (event.before(date) && newEvent.after(date)) || date.equals(event) || newDate.equals(newEvent) || date.equals(newEvent) || event.equals(newDate)) {
                flag=false;
                break;
            }
        }

        if (flag) { //se a data da sessão estiver livre postar a sessão
            stmt = connection.prepareStatement("INSERT INTO CINEMA_SESSION VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setTimestamp(1,new Timestamp(event.getTime()));
            stmt.setString(2, cmdBuilder.getParameter((String.valueOf(MOVIE_ID))));
            stmt.setString(3, cmdBuilder.getId(String.valueOf(THEATER_ID)));
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            int id = 0;
            if (rs.next()) id = rs.getInt(1);

            return new PostView<>("Session: ", id);
        }
        else {
            return new PostView<>("Session: ", "NOT POSTED!");
        }
    }
}
