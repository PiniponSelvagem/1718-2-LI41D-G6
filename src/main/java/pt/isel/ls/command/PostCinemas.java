package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public class PostCinemas implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) {
        return "INSERT INTO CINEMA VALUES ('"+
                cmdBuilder.getParameters("name")+"','"+
                cmdBuilder.getParameters("city")+"');";
    }
}
