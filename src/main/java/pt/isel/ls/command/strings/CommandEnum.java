package pt.isel.ls.command.strings;

public enum CommandEnum {

    ROOT_DIR("root"),
    ARGS_SEPERATOR(" "),
    DIR_SEPARATOR("/"),
    ID_PREFIX("{"),             ID_SUFFIX("}"),

    POST("POST"),
    GET("GET"),

    MOVIES("movies"),           MOVIES_ID("mid"),
    CINEMAS("cinemas"),         CINEMAS_ID("cid"),
    THEATERS("theaters"),       THEATERS_ID("tid"),
    SESSIONS("sessions"),       SESSIONS_ID("sid"),

    TODAY("today"),

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
