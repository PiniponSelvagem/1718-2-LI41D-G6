package pt.isel.ls.core.strings;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.OPTIONS;
import static pt.isel.ls.core.strings.CommandEnum.SEATS_ROW;

public enum ExceptionEnum {

    COMMAND__NOT_FOUND("Command not found. Check available commands at: "+OPTIONS),

    PATH__NOT_FOUND("Path not found, or didnt start with \'"+DIR_SEPARATOR+"\'. Syntax: {method} {path}"),

    PARAMETERS__NOT_FOUND("This command requires parameters and they weren't found. More info at: "+OPTIONS),
    PARAMETERS__NO_VALUE_ASSIGNED("Parameter { %s } doesn't have an assigned value."),
    PARAMETERS__EXPECTED("Parameter not found. Input parameter was: %s"),
    PARAMETERS__INVALID("Invalid parameter type. %s."),

    HEADERS__NOT_FOUND("Header not found. Check available headers at: "+OPTIONS),
    HEADERS__NO_VALUE_ASSIGNED("Header { %s } doesn't have an assigned value."),
    HEADERS__EXPECTED("Header not found. Input headers was: %s"),
    HEADERS__INVALID("Header with invalid syntax. More info at: "+OPTIONS),

    DATETIME_INVALID_FORMAT("Invalid date format. [dd/MM/yyyy HH:mm] & [yyyy/MM/dd HH:mm]"),
    DATE_INVALID_FORMAT("Invalid date or/and format. [ddMMyyyy]"),
    TICKET_SEAT_INVALID("Invalid input for parameter "+SEATS_ROW.toString()+". Did you input a numeric value? [0..9]"),

    SERVER_PORT_INVALID_FORMAT("Invalid server port number. Port syntax is, example: port=8080"),
    SERVER_PORT_ALREADY_IN_USE("Could not start server on port %s because its already in use."),

    DEBUG__EXCEPTION("EXCEPTION AT: CommandBuilder.parsePath() when trying to: "+
            "args[1].subSequence(0, DIR_SEPARATOR.toString().length()).equals(DIR_SEPARATOR.toString())"),

    VIEW__CREATION_ERROR("ERROR while initializing view."),

    TMDB_EXCEPTION("Problem when trying to get movie information from TheMoviesDB."),

    SQL_ERROR("ERROR: %d -> %s");

    ;

    private final String str;
    ExceptionEnum(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
