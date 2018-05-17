package pt.isel.ls.apps.http_server.http;

public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    SEE_OTHER(303),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500),
    ;
    
    private final int _code;
    HttpStatusCode(int code){
        _code = code;
    }
    public int valueOf() {
        return _code;
    }
}
