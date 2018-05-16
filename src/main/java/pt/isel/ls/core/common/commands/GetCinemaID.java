package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMA;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATERS;

public class GetCinemaID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {

        ResultSet rs;
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CINEMA WHERE cid = ?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());

        if(!rs.next())
            return new InfoNotFoundView(data);

        data.add(D_CINEMA, new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));


        //Get theater names for cinema id
        PreparedStatement stmt2 = connection.prepareStatement("select t.tid, t.Theater_Name from THEATER as t inner join CINEMA as c on t.cid = c.cid and c.cid = ?");
        stmt2.setString(1, cmdBuilder.getId((CINEMA_ID.toString())));
        rs = stmt2.executeQuery();
        LinkedList<Theater> theaters = new LinkedList<>();

        if (!rs.next())
            return new InfoNotFoundView(data);

        do {
            theaters.add(new Theater(rs.getInt(1), rs.getString(2), NA, NA, NA, NA));
        } while(rs.next());
        data.add(D_THEATERS, theaters);

        PreparedStatement stmt4 = connection.prepareStatement("select DISTINCT m.Title as Title, m.Duration as Duration, m.Release_Year as Release_Year, m.mid" +
                " from MOVIE as m inner join CINEMA_SESSION as cs on m.mid = cs.mid" +
                " inner join THEATER as t on t.tid = cs.tid" +
                " inner join CINEMA as c on c.cid = t.cid" +
                " WHERE c.cid = ?");
        stmt4.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        rs = stmt4.executeQuery();
        if (!rs.next())
            return new InfoNotFoundView(data);

        LinkedList<Movie> movies = new LinkedList<>();
        do {
            movies.add(new Movie(rs.getInt(4), rs.getString(1), rs.getInt(3), rs.getInt(2)));
        } while (rs.next());
        data.add(D_MOVIES, movies);

        return new GetCinemaIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}