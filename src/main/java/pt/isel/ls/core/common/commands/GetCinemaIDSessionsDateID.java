package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.CinemasSQL;
import pt.isel.ls.core.common.commands.db_queries.MoviesSQL;
import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.common.commands.db_queries.TheatersSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.model.Cinema;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.DATE_INVALID_FORMAT;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.*;

public class GetCinemaIDSessionsDateID extends Command {

    @Override
    public String getMethodName() {
        return GET.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+DATE+DIR_SEPARATOR+DATE_ID_FULL;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws ParameterException, SQLException {
        String cinemaID = cmdBuilder.getId(CINEMA_ID);
        LocalDate localDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATH_FORMAT.toString());
        
        try {
            localDate = LocalDate.parse(cmdBuilder.getId(DATE_ID), formatter);
        } catch(DateTimeParseException e){
            throw new ParameterException(DATE_INVALID_FORMAT);
        }

        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        Cinema cinema = CinemasSQL.queryID(con, cinemaID);
        data.add(D_CINEMA, cinema);

        data.add(D_SESSIONS, SessionsSQL.queryForCinemaAndDate(con, cinemaID, localDate.toString()));
        data.add(D_THEATERS, TheatersSQL.queryForCinema(con, cinemaID));
        data.add(D_MOVIES,   MoviesSQL.queryForCinema(con, cinemaID));
        data.add(D_CID,      cinemaID);

        Date date = Date.valueOf(localDate);
        data.add(D_DATE, date);

        return data;
    }

    @Override
    public boolean isSQLRequired() {
        return true;
    }
}