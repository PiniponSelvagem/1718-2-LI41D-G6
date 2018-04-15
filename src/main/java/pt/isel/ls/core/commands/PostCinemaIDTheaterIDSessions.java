package pt.isel.ls.core.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.PostView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class PostCinemaIDTheaterIDSessions extends Command {

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws CommandException, SQLException {
        /*  ALTERNATIVA
        "SELECT s.Date FROM CINEMA_SESSION AS s " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid "+
                "WHERE CAST(s.Date AS DATETIME) + s.Date BETWEEN ? AND ADDTIME(? ,  m.Duration * ?)"
        */
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.Date FROM CINEMA_SESSION AS s " +
                "INNER JOIN MOVIE AS m ON m.mid=s.mid "+
                "WHERE (s.Date BETWEEN ? AND ADDTIME(? ,  m.Duration * ?)) " +
                "AND s.tid=?"
        );
        
        stmt.setString(1, cmdBuilder.getParameter((String.valueOf(DATE_PARAM))));
        stmt.setString(2, cmdBuilder.getParameter((String.valueOf(DATE_PARAM))));
        stmt.setInt(3, 60);
        stmt.setString(4, cmdBuilder.getId(String.valueOf(THEATER_ID)));
        stmt.execute();

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            stmt = connection.prepareStatement("INSERT INTO CINEMA_SESSION VALUES (?, ?, ?)");
            stmt.setString(1, cmdBuilder.getParameter((String.valueOf(DATE_PARAM))));
            stmt.setString(2, cmdBuilder.getParameter((String.valueOf(MOVIE_ID))));
            stmt.setString(3, cmdBuilder.getId(String.valueOf(THEATER_ID)));
            stmt.execute();

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
