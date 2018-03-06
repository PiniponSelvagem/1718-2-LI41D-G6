package pt.isel.ls.command;

import java.sql.Connection;

public class PostMovies implements Command {

    @Override
    public String execute(Connection con) {
        return "Command: PostMovies";
    }
}
