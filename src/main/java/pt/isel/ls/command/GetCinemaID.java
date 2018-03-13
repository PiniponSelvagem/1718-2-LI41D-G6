package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetCinemaID implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        //return "SELECT * FROM CINEMA WHERE mid="+cmdBuilder.popId();
    }

    @Override
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CINEMA WHERE mid = ?");
        stmt.setInt(1, cmdBuilder.popId());

    }


}
