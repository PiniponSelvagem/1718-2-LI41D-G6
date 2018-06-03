package pt.isel.ls.core.common.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.commands.db_queries.SessionsSQL;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.sql.Sql;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.DATETIME_INVALID_FORMAT;
import static pt.isel.ls.core.strings.ExceptionEnum.SQL_ERROR;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_CID;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_POST;

public class PostCinemaIDTheaterIDSessions extends Command {
    private final static Logger log = LoggerFactory.getLogger(PostCinemaIDTheaterIDSessions.class);

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
    public DataContainer execute(CommandBuilder cmdBuilder) throws ParameterException {
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
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            data.add(D_POST,
                    SessionsSQL.postSession(con,
                        cmdBuilder.getId(THEATER_ID),
                        date,
                        movieID
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

        data.add(D_CID, cinemaID);
        return data;
    }
}
