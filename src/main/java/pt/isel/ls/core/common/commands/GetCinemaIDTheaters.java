package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemaIDTheatersView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATERS;

public class GetCinemaIDTheaters extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX+DIR_SEPARATOR+THEATERS;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM THEATER AS t WHERE t.cid=?");
        stmt.setString(1, cmdBuilder.getId(CINEMA_ID.toString()));
        ResultSet rs = stmt.executeQuery();

        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        LinkedList<Theater> theaters = new LinkedList<>();
        while(rs.next()){
            theaters.add(
                    new Theater(rs.getInt(1), rs.getString(5), rs.getInt(3), rs.getInt(4), rs.getInt(2), rs.getInt(6)
            ));
        }
        data.add(D_THEATERS, theaters);

        return new GetCinemaIDTheatersView(data, Integer.parseInt(cmdBuilder.getId(CINEMA_ID.toString())));
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
