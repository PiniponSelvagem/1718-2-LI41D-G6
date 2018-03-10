package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public class GetCinemaIDTheatersID implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) {
        return "SELECT * FROM THEATER AS t INNER JOIN CINEMA AS c WHERE c.cid="+
                cmdBuilder.popId()+" AND t.tid="+cmdBuilder.popId();
    }
}
