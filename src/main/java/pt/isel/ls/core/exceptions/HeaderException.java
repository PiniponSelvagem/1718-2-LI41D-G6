package pt.isel.ls.core.exceptions;

import pt.isel.ls.core.strings.ExceptionEnum;

public class HeaderException extends CommonException {

    public HeaderException(ExceptionEnum msg) {
        this.msg = msg.toString();
    }

    public HeaderException(ExceptionEnum msg, String replace) {
        this.msg = String.format(msg.toString(), replace);
    }
}
