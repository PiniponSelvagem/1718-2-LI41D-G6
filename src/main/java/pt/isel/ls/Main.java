package pt.isel.ls;

import pt.isel.ls.command.Command;
import pt.isel.ls.command.exceptions.CommandNotFoundException;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.command.utils.CommandUtils;
import pt.isel.ls.sql.Sql;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static CommandUtils cmdUtils = new CommandUtils();

    public static void main(String[] args) {
        try {
            String result;
            if (args.length == 2)
                result = executeCommand(args[0]+" "+args[1], null);
            else if (args.length == 3)
                result = executeCommand(args[0]+" "+args[1], args[2]);
            else
                throw new CommandNotFoundException();
            System.out.println("RESULT COMMAND -> "+result);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (CommandNotFoundException | InvalidCommandParametersException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String executeCommand(String str, String params) throws SQLException, CommandNotFoundException, InvalidCommandParametersException {
        Connection con = null;
        String result;
        try {
            con = Sql.CreateConnetion();
            CommandBuilder cmdBuilder = new CommandBuilder(str, params, cmdUtils);
            Command cmd = cmdUtils.getCmdTree().search(cmdBuilder);

            if (cmd == null)
                throw new CommandNotFoundException();

            result = cmd.execute(cmdBuilder);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return result;
    }
}
