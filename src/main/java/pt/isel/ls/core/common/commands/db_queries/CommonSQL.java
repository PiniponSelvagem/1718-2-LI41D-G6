package pt.isel.ls.core.common.commands.db_queries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.apps.console.Console;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonSQL {
    private final static Logger log = LoggerFactory.getLogger(Console.class);

    public static Timestamp dateStringToTimestamp(String date) {
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date time = formatter.parse(date);
            return new Timestamp(time.getTime());
        } catch(ParseException e) {
            log.error("Could not convert String to Timestamp. String was '{}'", date);
            return null;
        }
    }
}
