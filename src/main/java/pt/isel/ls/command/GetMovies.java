package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public class GetMovies implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) {
        return "SELECT Title, Release_Year FROM MOVIE";
    }
}
