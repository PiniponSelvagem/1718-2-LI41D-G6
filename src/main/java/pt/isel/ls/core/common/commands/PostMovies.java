package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.PARAMETERS__INVALID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SQL;

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
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws ParameterException, SQLException {
        int year, duration;
        try {
            year = Integer.parseInt(cmdBuilder.getParameter(YEAR));
            duration = Integer.parseInt(cmdBuilder.getParameter(DURATION));
        } catch (NumberFormatException e) {
            throw new ParameterException(PARAMETERS__INVALID, e.getMessage());
        }

        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_SQL,
                MoviesSQL.postMovie(con,
                    cmdBuilder.getParameter(TITLE),
                    year,
                    duration
                )
        );
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
