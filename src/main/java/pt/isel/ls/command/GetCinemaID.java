package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public class GetCinemaID implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) {
        return "SELECT * FROM CINEMA WHERE mid="+cmdBuilder.popId();
    }
}
