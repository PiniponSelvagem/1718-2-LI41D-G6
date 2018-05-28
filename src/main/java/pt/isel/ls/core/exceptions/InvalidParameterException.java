package pt.isel.ls.core.exceptions;

import pt.isel.ls.core.strings.ExceptionEnum;

public class InvalidParameterException extends Exception {

    private String msg;

    public InvalidParameterException(ExceptionEnum msg) {
        this.msg = String.valueOf(msg);
    }

    public InvalidParameterException(ExceptionEnum msg, String replace) {
        this.msg = String.format(msg.toString(), replace);
    }

    public String getMessage() {
        return "InvalidParameterException: " + msg;
    }
}