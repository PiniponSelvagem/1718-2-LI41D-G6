package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class PostCinemaIDTheaters implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException {
        int seats = Integer.parseInt(cmdBuilder.getParameters(String.valueOf(ROWS))) *
                    Integer.parseInt(cmdBuilder.getParameters(String.valueOf(SEATS_ROW)));

        return "INSERT INTO THEATER VALUES ("+
                seats+","+
                cmdBuilder.getParameters(String.valueOf(SEATS_ROW))+","+
                cmdBuilder.getParameters(String.valueOf(ROWS))+","+
                cmdBuilder.getParameters(String.valueOf(NAME))+","+
                cmdBuilder.getParameters(String.valueOf(CINEMAS_ID))+");";
    }
}
