package pt.isel.ls.core.exceptions;

import pt.isel.ls.core.strings.ExceptionEnum;

public class ParameterException extends CommonException {

    public ParameterException(ExceptionEnum msg) {
        this.msg = msg.toString();
    }

    public ParameterException(ExceptionEnum msg, String replace) {
        this.msg = String.format(msg.toString(), replace);
    }
}
