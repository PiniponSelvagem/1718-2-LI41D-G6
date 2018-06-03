package pt.isel.ls.core.common.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.sql.Sql;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.PARAMETERS__INVALID;
import static pt.isel.ls.core.strings.ExceptionEnum.SQL_ERROR;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_POST;

public class PostMovies extends Command {
    private final static Logger log = LoggerFactory.getLogger(PostMovies.class);

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder) throws ParameterException {
        int year, duration;
        try {
            year = Integer.parseInt(cmdBuilder.getParameter(YEAR));
            duration = Integer.parseInt(cmdBuilder.getParameter(DURATION));
        } catch (NumberFormatException e) {
            throw new ParameterException(PARAMETERS__INVALID, e.getMessage());
        }

        DataContainer data = new DataContainer(this.getClass().getSimpleName());
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
                log.error(String.format(SQL_ERROR.toString(), e1.getErrorCode(), e1.getMessage()), this.hashCode());
            }
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.error(String.format(SQL_ERROR.toString(), e.getErrorCode(), e.getMessage()), this.hashCode());
                }
            }
        }

        return data;
    }
}
