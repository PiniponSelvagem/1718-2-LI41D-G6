package pt.isel.ls.command;

import java.sql.Connection;

public interface Command {
    
    String execute(Connection con);
}
