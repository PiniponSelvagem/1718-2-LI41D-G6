package pt.isel.ls.command.exceptions;

import static pt.isel.ls.command.strings.ExceptionEnum.INVALID_PARAMETERS;

public class InvalidCommandParametersException extends Exception {

    public InvalidCommandParametersException() {
        super();
    }

    public String getMessage() {
        return String.valueOf(INVALID_PARAMETERS);
    }
}
