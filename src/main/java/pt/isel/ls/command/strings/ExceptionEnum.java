package pt.isel.ls.command.strings;

import static pt.isel.ls.command.strings.CommandEnum.HELP;

public enum ExceptionEnum {

    NOT_FOUND("Command not found. Check available commands with: "+HELP),
    INVALID_PARAMETERS("Invalid command parameters. Check available parameters for this command with: "+HELP);


    private final String str;
    ExceptionEnum(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
