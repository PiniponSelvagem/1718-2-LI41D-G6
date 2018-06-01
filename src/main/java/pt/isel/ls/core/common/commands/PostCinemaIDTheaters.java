package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.commands.db_queries.TheatersSQL;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.sql.Sql;

import java.sql.*;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.PARAMETERS__INVALID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_POST;

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
    public DataContainer execute(CommandBuilder cmdBuilder) throws CommandException {
        int cinemaID = Integer.parseInt(cmdBuilder.getId(CINEMA_ID));
        int rows, seatsRow;
        try {
            rows = Integer.parseInt(cmdBuilder.getParameter(ROWS));
            seatsRow = Integer.parseInt(cmdBuilder.getParameter(SEATS_ROW));
        } catch (NumberFormatException e) {
            throw new CommandException(PARAMETERS__INVALID, e.getMessage());
        }

        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            data.add(D_POST,
                    TheatersSQL.postTheater(con,
                        cinemaID,
                        cmdBuilder.getParameter(NAME),
                        rows,
                        seatsRow
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

        data.add(D_CID, cinemaID);
        return data;
    }
}
