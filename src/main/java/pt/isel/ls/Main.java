package pt.isel.ls;

import pt.isel.ls.command.Command;
import pt.isel.ls.command.utils.CommandBuilder;
import pt.isel.ls.command.utils.CommandUtils;
import pt.isel.ls.sql.Sql;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static CommandUtils cmdUtils = new CommandUtils();

    public static void main(String[] args) {
        try {
            String result = executeCommand(args[0]+" "+args[1]);    //TODO: add support for args[2]
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String executeCommand(String str) throws SQLException {
        Connection con = null;
        String result;
        try {
            con = Sql.CreateConnetion();
            CommandBuilder cmdBuilder = new CommandBuilder(str, cmdUtils);
            Command com = cmdUtils.getCmdTree().search(cmdBuilder);

            //TODO: [Exception] Command not found.
            //if (com instanceof NotFound)
            //    System.out.println("NOT FOUND");

            result = com.execute(cmdBuilder);
        }
        finally {
            if (con != null) {
                con.close();
            }
        }
        return result;
    }
}
