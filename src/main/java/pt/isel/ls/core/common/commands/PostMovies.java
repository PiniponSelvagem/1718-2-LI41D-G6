package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.exceptions.InvalidParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.sql.Sql;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.PARAMETERS__INVALID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_POST;

public class PostMovies extends Command {

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder) throws InvalidParameterException {
        int year, duration;
        try {
            year = Integer.parseInt(cmdBuilder.getParameter(YEAR));
            duration = Integer.parseInt(cmdBuilder.getParameter(DURATION));
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(PARAMETERS__INVALID, e.getMessage());
        }

        DataContainer data = new DataContainer(this.getClass().getSimpleName(), cmdBuilder.getHeader());
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            data.add(D_POST,
                    MoviesSQL.postMovie(con,
                        cmdBuilder.getParameter(TITLE),
                        year,
                        duration
                    )
            );
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
