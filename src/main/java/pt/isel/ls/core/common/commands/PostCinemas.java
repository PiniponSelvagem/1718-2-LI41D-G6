package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.CinemasSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SQL;

public class PostCinemas extends Command {

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws ParameterException, SQLException {
        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_SQL,
            CinemasSQL.postCinema(con,
                cmdBuilder.getParameter(NAME),
                cmdBuilder.getParameter(CITY)
            )
        );
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
