package pt.isel.ls.command;

import java.sql.Connection;

public class GetCinemas implements Command {

    @Override
    public String execute(Connection con) {
        return "Command: GetCinemas";
    }
}
