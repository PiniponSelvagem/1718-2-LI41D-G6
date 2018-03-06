package pt.isel.ls.command;

import java.sql.Connection;

public class PostCinemas implements Command {

    @Override
    public String execute(Connection con) {
        return "Command: PostCinemas";
    }
}
