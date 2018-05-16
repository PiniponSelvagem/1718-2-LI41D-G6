package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.view.command.CommandView;
import pt.isel.ls.view.command.GetCinemasView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CINEMAS;

public class GetCinemas extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS;
    }

    @Override
    public CommandView execute(CommandBuilder cmdBuilder, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * from CINEMA");
        ResultSet rs = stmt.executeQuery();
        DataContainer data = new DataContainer(cmdBuilder.getHeader());
        LinkedList<Cinema> cinemas = new LinkedList<>();

        while(rs.next()){
            cinemas.add(new Cinema(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        data.add(D_CINEMAS, cinemas);

        return new GetCinemasView(data);
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
