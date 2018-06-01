package pt.isel.ls.core.common.headers;

import java.util.HashSet;
import java.util.Set;

import static pt.isel.ls.core.strings.CommandEnum.*;

public enum HeadersAvailable {
    TEXT_PLAIN(""+TEXT+DIR_SEPARATOR+PLAIN),
    TEXT_HTML(""+TEXT+DIR_SEPARATOR+HTML),
    APP_JSON(""+APPLICATION+DIR_SEPARATOR+JSON),

    ;

    private final String headerType;
    private static Set<String> enumValues = new HashSet<>();

    //Do once, so later the contains will be faster checking if header is valid.
    static {
        for (HeadersAvailable headersAvailable : HeadersAvailable.values()) {
            enumValues.add(headersAvailable.toString());
        }
    }

    HeadersAvailable(String headerType) {
        this.headerType = headerType;
    }

    @Override
    public String toString() {
        return headerType;
    }

    public static boolean contains(String type) {
        return enumValues.contains(type);
    }
}
