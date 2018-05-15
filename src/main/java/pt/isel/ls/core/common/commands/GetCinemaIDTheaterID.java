package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheaterIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDTheaterID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+THEATER_ID_FULL;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        Movie movie;
        Cinema cinema;
        Theater theater;

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CINEMA AS c WHERE c.cid=?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        cinema = null;
        if (rs.next())
            cinema = new Cinema(rs.getInt(1),rs.getString(2),rs.getString(3));
        data.add(D_CINEMA, cinema);

        stmt = connection.prepareStatement("SELECT * FROM THEATER AS t WHERE t.tid=?");
        stmt.setString(1, cmdBuilder.getId(THEATER_ID.toString()));
        rs = stmt.executeQuery();

        if (!rs.next())
            return new InfoNotFoundView(data);
        theater=new Theater(rs.getInt(1), rs.getString(5), rs.getInt(3), rs.getInt(4), rs.getInt(2),
                Integer.parseInt(cmdBuilder.getId(THEATER_ID.toString())));
        data.add(D_THEATER, theater);

        stmt = connection.prepareStatement("SELECT * FROM CINEMA_SESSION AS cs WHERE cs.tid=?");
        stmt.setString(1, cmdBuilder.getId(THEATER_ID.toString()));
        rs = stmt.executeQuery();
        LinkedList<Session> sessions = new LinkedList<>();
        while (rs.next()) {
            stmt = connection.prepareStatement("SELECT * FROM MOVIE AS m WHERE m.mid=?");
            stmt.setInt(1, rs.getInt(3));
            ResultSet mrs = stmt.executeQuery();
            movie=null;
            if(mrs.next())
                movie=new Movie(mrs.getInt(1),mrs.getString(2),mrs.getInt(3),mrs.getInt(4));
            sessions.add(new Session(rs.getInt(1),rs.getTimestamp(2),
                    movie,theater,rs.getInt(5)));
        }
        data.add(D_SESSIONS, sessions);


        return new GetCinemaIDTheaterIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
