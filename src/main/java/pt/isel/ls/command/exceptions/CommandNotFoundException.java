package pt.isel.ls.command.exceptions;

import static pt.isel.ls.command.strings.ExceptionEnum.NOT_FOUND;

public class CommandNotFoundException extends Exception {

    public CommandNotFoundException() {
        super();
    }

    public String getMessage() {
        return String.valueOf(NOT_FOUND);
    }
}
