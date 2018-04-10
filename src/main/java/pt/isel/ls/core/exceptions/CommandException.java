package pt.isel.ls.core.exceptions;

import pt.isel.ls.core.strings.ExceptionEnum;

public class CommandException extends Exception {

    private String msg;

    public CommandException(ExceptionEnum msg) {
        this.msg = String.valueOf(msg);
    }

    public CommandException(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return "CommandException: "+msg;
    }
}
