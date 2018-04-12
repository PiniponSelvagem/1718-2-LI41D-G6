package pt.isel.ls.core.strings;

import static pt.isel.ls.core.strings.CommandEnum.OPTIONS;

public enum ExceptionEnum {

    COMMAND__NOT_FOUND("Command not found. Check available commands with: "+OPTIONS),

    PARAMETERS__NOT_FOUND("This command requires parameters and they weren't found. More info at: "+OPTIONS),
    PARAMETERS__NO_VALUE_ASSIGNED("Parameter %s doesn't have an assigned value."),
    PARAMETERS__EXPECTED("Parameter not found. Expected parameter was: %s"),

    HEADERS__NOT_FOUND("This command requires headers and they weren't found. More info at: "+OPTIONS),
    HEADERS__NO_VALUE_ASSIGNED("Header %s doesn't have an assigned value."),
    HEADERS__EXPECTED("Header not found. Expected headers was: %s"),
    HEADERS__INVALID("Header with invalid syntax. More info at: "+OPTIONS);

    private final String str;
    ExceptionEnum(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
