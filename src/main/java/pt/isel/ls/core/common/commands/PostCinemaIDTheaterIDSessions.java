package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.DATETIME_INVALID_FORMAT;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SQL;

public class PostCinemaIDTheaterIDSessions extends Command {

    @Override
    public String getMethodName() {
        return POST.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+CINEMA_ID_FULL
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+THEATER_ID_FULL+DIR_SEPARATOR+SESSIONS;
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder, Connection con) throws ParameterException, SQLException {
        String cinemaID = cmdBuilder.getId(CINEMA_ID);
        String movieID  = cmdBuilder.getParameter(MOVIE_ID);

        SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //format 1
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //format 2
        String dateStr = cmdBuilder.getParameter(DATE_PARAM);
        String check;
        Date date;

        try {
            check = dateStr+":00";
            sdf1.setLenient(false);
            date = sdf1.parse(check.trim());
        } catch (ParseException e) {
            try {
                check = dateStr+":00";
                sdf2.setLenient(false);
                date = sdf2.parse(check.trim());
            } catch (ParseException ex) {
                throw new ParameterException(DATETIME_INVALID_FORMAT);
            }
        }

        DataContainer data = new DataContainer(this.getClass().getSimpleName());
        data.add(D_SQL,
                SessionsSQL.postSession(con,
                    cmdBuilder.getId(THEATER_ID),
                    date,
                    movieID
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
