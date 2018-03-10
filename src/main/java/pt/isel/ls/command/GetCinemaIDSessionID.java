package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public class GetCinemaIDSessionID implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) {
        return "SELECT * FROM CINEMA_SESSIONS AS s INNER JOIN CINEMA AS c WHERE c.cid="+
                cmdBuilder.popId()+" AND s.sid="+cmdBuilder.popId();
    }
}
