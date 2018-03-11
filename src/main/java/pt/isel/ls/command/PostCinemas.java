package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import static pt.isel.ls.command.strings.CommandEnum.CITY;
import static pt.isel.ls.command.strings.CommandEnum.NAME;

public class PostCinemas implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException {
        return "INSERT INTO CINEMA VALUES ('"+
                cmdBuilder.getParameters(String.valueOf(NAME))+"','"+
                cmdBuilder.getParameters(String.valueOf(CITY))+"');";
    }
}
