package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public class GetCinemas implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) {
        return "SELECT Cinema_Name, City FROM CINEMA";
    }
}
