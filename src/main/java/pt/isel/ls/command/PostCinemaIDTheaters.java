package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class PostCinemaIDTheaters implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) throws InvalidCommandParametersException {
        int seats = Integer.parseInt(cmdBuilder.getParameter(String.valueOf(ROWS))) *
                    Integer.parseInt(cmdBuilder.getParameter(String.valueOf(SEATS_ROW)));

        return "INSERT INTO THEATER VALUES ("+
                seats+","+
                cmdBuilder.getParameter(String.valueOf(SEATS_ROW))+","+
                cmdBuilder.getParameter(String.valueOf(ROWS))+","+
                cmdBuilder.getParameter(String.valueOf(NAME))+","+
                cmdBuilder.getParameter(String.valueOf(CINEMAS_ID))+");";
    }
}
