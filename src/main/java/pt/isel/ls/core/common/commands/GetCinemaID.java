package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class GetCinemaID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {


        //int na = Integer.parseInt(cmdBuilder.getId(String.valueOf(NA)));  <-- não funciona
        ResultSet rs;

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CINEMA WHERE cid = ?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        rs = stmt.executeQuery();
        if (rs.wasNull())
            return new InfoNotFoundView();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        if(rs.next())
            data.add(new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));


        //obter nome das salas do cinema com id CINEMA_ID
        PreparedStatement stmt2 = connection.prepareStatement("select t.Theater_Name from THEATER as t inner join CINEMA as c on t.cid = c.cid and c.cid = ?");
        stmt2.setString(1, cmdBuilder.getId((CINEMA_ID.toString())));
        rs = stmt2.executeQuery();
        int theaterCount = 0;
        while(rs.next()){
            theaterCount++;
            //só quero o TheaterName há melhor forma de fazer?
            data.add(new Theater(0, rs.getString(1), 0, 0, 0, 0));
        }

        /*// este código fica em comentário pois pode vir a ser útil
        obter informação sobre as sessões no CINEMA_ID para o dia de hoje
        PreparedStatement stmt3 = connection.prepareStatement("select t.Theater_Name as salas, m.Title as Movies, cs.Date as Date, cs.SeatsAvailable as Seats_Available from CINEMA as c" +
                " inner join THEATER as t on t.cid = c.cid" +
                " inner join CINEMA_SESSION as cs on cs.tid = t.tid" +
                " inner join MOVIE as m on m.mid = cs.mid" +
                " where (CAST(cs.Date AS DATE))= ? and c.cid = ?");

        stmt3.setDate(1, date);
        stmt3.setString(2, cmdBuilder.getId(CINEMA_ID.toString()));
        rs = stmt3.executeQuery();
        if (rs.wasNull())
            return new InfoNotFoundView();
        *//*if (!rs.next())
            return new InfoNotFoundView();*//*
        //só quero alguns valores
        while(rs.next()){
            data.add(new Session(0, rs.getTimestamp(3), null, null, 0));
            data.add(new Theater(0, rs.getString(1), 0, 0, 0, 0));
            data.add(new Movie(0, rs.getString(2), 0, 0));
        }*/


        return new GetCinemaIDView(data, theaterCount);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
