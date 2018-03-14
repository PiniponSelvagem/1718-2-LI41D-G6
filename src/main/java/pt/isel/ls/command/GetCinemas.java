package pt.isel.ls.command;

import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetCinemas implements Command {

    @Override
    public void execute(CommandBuilder cmdBuilder) {
        //return  null;
    }

    /**
     * Get all the Cinemas
     *
     * @param cmdBuilder
     * @param connection
     * @throws InvalidCommandParametersException
     * @throws CommandNotFoundException
     * @throws SQLException
     */
    @Override //Comando a trabalhar faz get da lista de cinemas existentes e emite mensagem
    public void execute(CommandBuilder cmdBuilder, Connection connection) throws InvalidCommandParametersException, CommandNotFoundException, SQLException {

        PreparedStatement stmt = connection.prepareStatement("SELECT cid from CINEMA");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            System.out.println("Cinema id: "+rs.getInt(1));
        }

    }
}
