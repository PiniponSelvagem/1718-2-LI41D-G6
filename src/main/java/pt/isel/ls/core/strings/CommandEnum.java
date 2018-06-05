package pt.isel.ls.core.strings;

public enum CommandEnum {
    NOT_FOUND("NOT_FOUND"),

    //Special definitions
    ROOT_DIR("root"),
    ARGS_SEPARATOR(" "),
    DIR_SEPARATOR("/"),
    ID_PREFIX("{"),             ID_SUFFIX("}"),
    PARAMS_SEPARATOR("&"),
    PARAMS_EQUALTO("="),
    PARAMS_VALS_SEPARATOR("+"),
    PARAMS_VALS_SEPERATOR_REPLACEMENT(" "),
    HEADERS_SEPERATOR("\\|"),
    HEADERS_EQUALTO(":"),
    DATE_PATH_FORMAT("ddMMyyyy"),

    //Methods
    OPTIONS("OPTIONS"),
    EXIT("EXIT"),
    LISTEN("LISTEN"),
    POST("POST"),
    GET("GET"),
    DELETE("DELETE"),

    //Directories
    MOVIES("movies"),           MOVIE_ID("mid"),    MOVIE_ID_FULL(String.format("%s%s%s", ID_PREFIX, MOVIE_ID, ID_SUFFIX)),
    CINEMAS("cinemas"),         CINEMA_ID("cid"),   CINEMA_ID_FULL(String.format("%s%s%s", ID_PREFIX, CINEMA_ID, ID_SUFFIX)),
    THEATERS("theaters"),       THEATER_ID("tid"),  THEATER_ID_FULL(String.format("%s%s%s", ID_PREFIX, THEATER_ID, ID_SUFFIX)),
    SESSIONS("sessions"),       SESSION_ID("sid"),  SESSION_ID_FULL(String.format("%s%s%s", ID_PREFIX, SESSION_ID, ID_SUFFIX)),
    TICKETS("tickets"),         TICKET_ID("tkid"),  TICKET_ID_FULL(String.format("%s%s%s", ID_PREFIX, TICKET_ID, ID_SUFFIX)),
    AVAILABLE("available"),
    DATE("date"),               DATE_ID("dmy"),     DATE_ID_FULL(String.format("%s%s%s", ID_PREFIX, DATE_ID, ID_SUFFIX)),
    TODAY("today"),
    TMDB("tmdb"),               TMDB_ID("tmdbID"),  TMDB_ID_FULL(String.format("%s%s%s", ID_PREFIX, TMDB_ID, ID_SUFFIX)),

    //Headers_Cmds
    ACCEPT("accept"),
    FILE_NAME("file-name"),

    //Headers_Directories
    TEXT("text"),
    APPLICATION("application"),

    //Headers
    PLAIN("plain"),
    HTML("html"),
    JSON("json"),

    //Parameters
    NAME("name"),
    CITY("city"),
    TITLE("title"),
    YEAR("releaseYear"),
    DURATION("duration"),
    ROWS("row"),
    SEATS_ROW("seat"),
    DATE_PARAM("date"),
    SERVER_PORT("port"),

    ;

    private final String str;
    CommandEnum(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
