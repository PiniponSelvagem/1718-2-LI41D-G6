package pt.isel.ls.command;

import java.sql.Connection;

public class NotFound implements Command {

    @Override
    public String execute(Connection con) {
        return null;
    }
}
