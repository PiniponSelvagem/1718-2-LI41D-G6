package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetMovieIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetMovieID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+MOVIE_ID_FULL;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MOVIE WHERE mid = ?");
        stmt.setString(1, cmdBuilder.getId(MOVIE_ID.toString()));
        ResultSet rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        if (!rs.next())
            return new InfoNotFoundView(data);
        else
            data.add(D_MOVIE, new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));

        PreparedStatement stmt2 = connection.prepareStatement("select c.cid as ID, c.Name as Name, c.City as City from CINEMA as c INNER JOIN THEATER as t on c.cid = t.cid " +
                                "inner join CINEMA_SESSION as cs on cs.tid = t.tid " +
                                "inner join MOVIE as m on m.mid = cs.mid and m.mid = ? " +
                                "group by c.cid, c.Name, c.City");
        stmt2.setString(1, cmdBuilder.getId(MOVIE_ID.toString()));
        rs = stmt2.executeQuery();
        LinkedList<Cinema> cinemas = new LinkedList<>();
        while(rs.next()){
            cinemas.add(new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        data.add(D_CINEMAS, cinemas);

        PreparedStatement stmt3 = connection.prepareStatement("select * from CINEMA_SESSION as cs INNER JOIN THEATER as t on cs.tid = t.tid " +
                                "inner join CINEMA as c on t.cid = c.cid " +
                                "inner join MOVIE as m on m.mid = cs.mid and m.mid = ? ");
        stmt3.setString(1, cmdBuilder.getId(MOVIE_ID.toString()));
        rs = stmt3.executeQuery();
        LinkedList<Session> sessions = new LinkedList<>();
        //LinkedList<Cinema> cinemas2 = new LinkedList<>();
        while(rs.next()){
            sessions.add(new Session(rs.getInt(1), rs.getTimestamp(2),
                    new Movie(rs.getInt(3), rs.getString(16), rs.getInt(17), rs.getInt(18)),
                    new Theater(rs.getInt(4), rs.getString(10), rs.getInt(8), rs.getInt(9), rs.getInt(5), rs.getInt(11)),
                    rs.getInt(11)));
            //cinemas2.add(new Cinema(rs.getInt(12), rs.getString(13), rs.getString(14)));
        }
        data.add(D_SESSIONS, sessions);
        //data.add(DC_CINEMAS, cinemas2);
        return new GetMovieIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
