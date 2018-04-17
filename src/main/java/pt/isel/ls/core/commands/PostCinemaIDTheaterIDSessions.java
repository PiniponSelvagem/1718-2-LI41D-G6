package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class PostCinemaIDTheaterIDSessions extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        /*  ALTERNATIVA
        "SELECT s.Date FROM CINEMA_SESSION AS s " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid "+
                "WHERE CAST(s.Date AS DATETIME) + s.Date BETWEEN ? AND ADDTIME(? ,  m.Duration * ?)"
        */
        /*PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.Date FROM CINEMA_SESSION AS s " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid "+
                "WHERE (s.Date BETWEEN ? AND ADDTIME(? ,  m.Duration * ?)) " +
                "AND s.tid=?"
        );
        */

        //TODO: falta a logica de não se inserir as sessions por cima das outras
        String ts = cmdBuilder.getParameter(String.valueOf(DATE_PARAM));
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        try {
            date = format.parse(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO CINEMA_SESSION VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(MOVIE_ID))));
        stmt.setString(3, cmdBuilder.getId(String.valueOf(THEATER_ID)));
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        int id = 0;
        if (rs.next()) id = rs.getInt(1);
        return new PostView<>("Session: ", id);
    }
}
