package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public class GetMovieID implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) {
        return "SELECT * FROM MOVIE WHERE mid="+cmdBuilder.popId();
    }
}
