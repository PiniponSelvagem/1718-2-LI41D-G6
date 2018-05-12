package pt.isel.ls.core.utils;

import pt.isel.ls.core.common.headers.Header;

import java.util.HashMap;

public class DataContainer {
    private Header header;
    private HashMap<DataEnum, Object> map = new HashMap<>();

    public DataContainer(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }

    public void add(DataEnum key, Object obj) {
        map.put(key, obj);
    }

    public Object getData(DataEnum key) {
        return map.get(key);
    }

    public enum DataEnum {
        D_CINEMAS,
        D_CINEMA,

        D_THEATERS,
        D_THEATER,

        D_SESSIONS,
        D_SESSION,

        D_MOVIES,
        D_MOVIE,

        D_TICKETS,
        D_TICKET,

        D_AVAILABLE_SEATS,

        ;
    }
}
