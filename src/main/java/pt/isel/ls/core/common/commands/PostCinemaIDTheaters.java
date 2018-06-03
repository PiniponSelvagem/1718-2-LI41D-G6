package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.TheatersSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.PARAMETERS__INVALID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SQL;

public class PostCinemaIDTheaters extends Command {

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL+DIR_SEPARATOR+THEATERS;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws ParameterException, SQLException {
        String cinemaID = cmdBuilder.getId(CINEMA_ID);
        int rows, seatsRow;
        try {
            rows = Integer.parseInt(cmdBuilder.getParameter(ROWS));
            seatsRow = Integer.parseInt(cmdBuilder.getParameter(SEATS_ROW));
        } catch (NumberFormatException e) {
            throw new ParameterException(PARAMETERS__INVALID, e.getMessage());
        }

        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_SQL,
                TheatersSQL.postTheater(con,
                    cinemaID,
                    cmdBuilder.getParameter(NAME),
                    rows,
                    seatsRow
                )
        );
        data.add(D_CID, cinemaID);
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
