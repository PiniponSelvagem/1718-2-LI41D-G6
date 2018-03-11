package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public class PostCinemaIDTheaterIDSessions implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) {
        return "INSERT INTO CINEMA_SESSIONS VALUES ("+
                cmdBuilder.getParameters("date")+","+
                cmdBuilder.getParameters("mid")+","+
                cmdBuilder.getParameters("tid")+");";
    }
}
