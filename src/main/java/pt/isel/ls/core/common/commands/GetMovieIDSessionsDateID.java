package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.common.commands.db_queries.TheatersSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetMovieIDSessionsDateID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+MOVIE_ID_FULL
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+DATE+DIR_SEPARATOR+DATE_ID_FULL;
}

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws ParameterException, SQLException {
        String movieID = cmdBuilder.getId(MOVIE_ID);
        LocalDate localDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATH_FORMAT.toString());
        localDate = LocalDate.parse(cmdBuilder.getId(DATE_ID), formatter);
        Date date = Date.valueOf(localDate);
        String dateStr = date.toString();

        DataContainer data = new DataContainer(this.getClass().getSimpleName());

        if (cmdBuilder.hasParameter(CITY)) {
            data.add(D_SESSIONS, SessionsSQL.queryPlayingMovieIDForDateAndCity(con,
                    movieID,
                    dateStr,
                    cmdBuilder.getParameter(CITY)
                    )
            );
            data.add(D_THEATERS, TheatersSQL.queryPlayingMovieIDForDateAndCity(con,
                    movieID,
                    dateStr,
                    cmdBuilder.getParameter(CITY)
                    )
            );
        }
        else if (cmdBuilder.hasParameter(CINEMA_ID)) {
            data.add(D_SESSIONS, SessionsSQL.queryPlayingMovieIDForDateAndCinemaID(con,
                    movieID,
                    dateStr,
                    Integer.parseInt(cmdBuilder.getParameter(CINEMA_ID))
                    )
            );
            data.add(D_THEATERS, TheatersSQL.queryPlayingMovieIDForDateAndCinemaID(con,
                    movieID,
                    dateStr,
                    Integer.parseInt(cmdBuilder.getParameter(CINEMA_ID))
                    )
            );
        }
        else if (cmdBuilder.hasParameter(AVAILABLE)) {
            data.add(D_SESSIONS, SessionsSQL.queryPlayingMovieIDForDateAndAvailableAbove(con,
                    movieID,
                    dateStr,
                    Integer.parseInt(cmdBuilder.getParameter(AVAILABLE))
                    )
            );
            data.add(D_THEATERS, TheatersSQL.queryPlayingMovieIDForDateAndAvailableAbove(con,
                    movieID,
                    dateStr,
                    Integer.parseInt(cmdBuilder.getParameter(AVAILABLE))
                    )
            );
        }
        else {
            data.add(D_SESSIONS, SessionsSQL.queryPlayingMovieIDForDate(con,
                    movieID,
                    dateStr
                    )
            );
            data.add(D_THEATERS, TheatersSQL.queryPlayingMovieIDForDate(con,
                    movieID,
                    dateStr
                    )
            );
        }

        data.add(D_MID,  movieID);
        data.add(D_DATE, date);
        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}
