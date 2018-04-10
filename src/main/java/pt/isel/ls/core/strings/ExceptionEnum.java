package pt.isel.ls.core.strings;

import static pt.isel.ls.core.strings.CommandEnum.HELP;

public enum ExceptionEnum {

    COMMAND__NOT_FOUND("Command not found. Check available commands with: "+HELP),

    PARAMETERS__NOT_FOUND("This command requires parameters and they weren't found. More info at: "+HELP),
    PARAMETERS__NO_VALUE_ASSIGNED("Parameter %s doesn't have an assigned value."),
    PARAMETERS__EXPECTED("Parameter not found. Expected parameter was: %s"),

    HEADERS__NOT_FOUND("This command requires headers and they weren't found. More info at: "+HELP),
    HEADERS__NO_VALUE_ASSIGNED("Header %s doesn't have an assigned value."),
    HEADERS__EXPECTED("Header not found. Expected headers was: %s"),
    HEADERS__INVALID("Header with invalid syntax. More info at: "+HELP);

    private final String str;
    ExceptionEnum(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
