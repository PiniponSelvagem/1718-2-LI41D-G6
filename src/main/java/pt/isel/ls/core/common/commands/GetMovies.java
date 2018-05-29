package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.sql.Sql;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_MOVIES;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_POST;

public class GetMovies extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder) {
        DataContainer data = new DataContainer(this.getClass().getSimpleName(), cmdBuilder.getHeader());
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            data.add(D_MOVIES, MoviesSQL.queryAll(con));
            con.commit();
        } catch (SQLException e) {
            data.add(D_POST, new PostData<>(e.getErrorCode(), e.getMessage()));
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            //TODO: catch excp handling
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }
}
