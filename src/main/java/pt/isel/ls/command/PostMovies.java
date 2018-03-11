package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import static pt.isel.ls.command.strings.CommandEnum.DURATION;
import static pt.isel.ls.command.strings.CommandEnum.TITLE;
import static pt.isel.ls.command.strings.CommandEnum.YEAR;

public class PostMovies implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException {
        return "INSERT INTO MOVIE VALUES ('"+
                cmdBuilder.getParameters(String.valueOf(TITLE))+"',"+
                cmdBuilder.getParameters(String.valueOf(YEAR))+","+
                cmdBuilder.getParameters(String.valueOf(DURATION))+");";
    }
}
