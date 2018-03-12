package pt.isel.ls.command.strings;

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

    //Methods
    POST("POST"),
    GET("GET"),

    //Directories
    MOVIES("movies"),           MOVIES_ID("mid"),
    CINEMAS("cinemas"),         CINEMAS_ID("cid"),
    THEATERS("theaters"),       THEATERS_ID("tid"),
    SESSIONS("sessions"),       SESSIONS_ID("sid"),
    TODAY("today"),

    //Parameters
    NAME("name"),
    CITY("city"),
    TITLE("title"),
    YEAR("year"),
    DURATION("duration"),
    ROWS("rows"),
    SEATS_ROW("seatsrow"),
    DATE("date");


    private final String str;
    CommandEnum(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
