package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDView;
import pt.isel.ls.view.command.InfoNotFoundView;

import java.sql.*;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMA;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATERS;

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

        ResultSet rs;
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CINEMA WHERE cid = ?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());

        if (rs.wasNull())
            return new InfoNotFoundView(data);

        if(rs.next())
            data.add(D_CINEMA, new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));
        /*if (!rs.next())
            return new InfoNotFoundView();*/


        //Get theater names for cinema id
        PreparedStatement stmt2 = connection.prepareStatement("select t.Theater_Name from THEATER as t inner join CINEMA as c on t.cid = c.cid and c.cid = ?");
        stmt2.setString(1, cmdBuilder.getId((CINEMA_ID.toString())));
        rs = stmt2.executeQuery();

        LinkedList<Theater> theaters = new LinkedList<>();
        while(rs.next()){
            theaters.add(new Theater(NA, rs.getString(1), NA, NA, NA, NA));
        }
        data.add(D_THEATERS, theaters);

        /*
        if (data.size() == 0)
            return new InfoNotFoundView(data);
            */

        return new GetCinemaIDView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}