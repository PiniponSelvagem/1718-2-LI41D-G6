package pt.isel.ls.core.exceptions;

import pt.isel.ls.core.strings.ExceptionEnum;

public class TheMoviesDBException extends CommonException {

    public TheMoviesDBException(ExceptionEnum msg) {
        this.msg = msg.toString();
    }

    public TheMoviesDBException(ExceptionEnum msg, String replace) {
        this.msg = String.format(msg.toString(), replace);
    }
}
