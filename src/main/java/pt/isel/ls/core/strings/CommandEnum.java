package pt.isel.ls.core.strings;

public enum CommandEnum {

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

    //Methods
    OPTIONS("OPTIONS"),
    EXIT("EXIT"),
    POST("POST"),
    GET("GET"),
    DELETE("DELETE"),

    //Directories
    MOVIES("movies"),           MOVIE_ID("mid"),
    CINEMAS("cinemas"),         CINEMA_ID("cid"),
    THEATERS("theaters"),       THEATER_ID("tid"),
    SESSIONS("sessions"),       SESSION_ID("sid"),
    TICKETS("tickets"),         TICKET_ID("tkid"),
    AVAILABLE("available"),
    DATE("date"),               DATE_ID("d"),
    TODAY("today"),

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
    ROWS("rows"),
    SEATS_ROW("seats"),
    DATE_PARAM("date");


    private final String str;
    CommandEnum(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
