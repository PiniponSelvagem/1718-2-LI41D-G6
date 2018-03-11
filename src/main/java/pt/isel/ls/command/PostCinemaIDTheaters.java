package pt.isel.ls.command;

import pt.isel.ls.command.utils.CommandBuilder;

public class PostCinemaIDTheaters implements Command {

    @Override
    public String execute(CommandBuilder cmdBuilder) {
        int seats = Integer.parseInt(cmdBuilder.getParameters("rows")) *
                Integer.parseInt(cmdBuilder.getParameters("seats_row"));
        //TODO: exception if cant parseInt

        return "INSERT INTO THEATER VALUES ("+
                seats+","+
                cmdBuilder.getParameters("seats_row")+","+
                cmdBuilder.getParameters("rows")+","+
                cmdBuilder.getParameters("name")+","+
                cmdBuilder.getParameters("cid")+");";
    }
}
