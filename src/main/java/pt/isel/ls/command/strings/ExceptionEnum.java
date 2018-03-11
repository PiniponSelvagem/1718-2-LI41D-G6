package pt.isel.ls.command.strings;

public enum ExceptionEnum {

    NOT_FOUND("Command not found."),
    INVALID_PARAMETERS("Invalid command parameters.");


    private final String str;
    ExceptionEnum(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
