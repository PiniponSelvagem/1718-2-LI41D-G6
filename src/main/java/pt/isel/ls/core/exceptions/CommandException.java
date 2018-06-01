package pt.isel.ls.core.exceptions;

import pt.isel.ls.core.strings.ExceptionEnum;

public class CommandException extends CommonException {

    public CommandException(ExceptionEnum msg) {
        this.msg = msg.toString();
    }

    public CommandException(ExceptionEnum msg, String replace) {
        this.msg = String.format(msg.toString(), replace);
    }
}
