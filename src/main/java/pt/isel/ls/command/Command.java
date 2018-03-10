package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public interface Command {
    
    String execute(CommandBuilder cmdBuilder);
}
