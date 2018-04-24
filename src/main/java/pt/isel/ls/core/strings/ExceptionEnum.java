package pt.isel.ls.core.strings;

import static pt.isel.ls.core.strings.CommandEnum.OPTIONS;

public enum ExceptionEnum {

    COMMAND__NOT_FOUND("Command not found. Check available commands at: "+OPTIONS),

    PARAMETERS__NOT_FOUND("This command requires parameters and they weren't found. More info at: "+OPTIONS),
    PARAMETERS__NO_VALUE_ASSIGNED("Parameter { %s } doesn't have an assigned value."),
    PARAMETERS__EXPECTED("Parameter not found. Input parameter was: %s"),

    HEADERS__NOT_FOUND("Header not found. Check available headers at: "+OPTIONS),
    HEADERS__NO_VALUE_ASSIGNED("Header { %s } doesn't have an assigned value."),
    HEADERS__EXPECTED("Header not found. Input headers was: %s"),
    HEADERS__INVALID("Header with invalid syntax. More info at: "+OPTIONS),

    DATE_INVALID_FORMAT("Invalid date format. [dd/MM/yyyy HH:mm] & [yyyy/MM/dd HH:mm]");

    private final String str;
    ExceptionEnum(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
