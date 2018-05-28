package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.TheatersSQL;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.sql.Sql;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_THEATERS;

public class GetCinemaIDTheaters extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL+DIR_SEPARATOR+THEATERS;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder) {
        int cinemaID  = Integer.parseInt(cmdBuilder.getId(CINEMA_ID));
        DataContainer data = new DataContainer(this.getClass().getSimpleName(), cmdBuilder.getHeader());
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            data.add(D_THEATERS, TheatersSQL.queryAll(con, cinemaID));
            data.add(D_CID, cinemaID);
            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return data;
    }
}
