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
                cmdBuilder.getParameter(String.valueOf(TITLE))+"',"+
                cmdBuilder.getParameter(String.valueOf(YEAR))+","+
                cmdBuilder.getParameter(String.valueOf(DURATION))+");";
    }
}
