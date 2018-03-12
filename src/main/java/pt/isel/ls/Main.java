package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerException;
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
                result = executeBuildedCommand(prepareUserInput(args[0]+" "+args[1]));
            else if (args.length == 3)
                result = executeBuildedCommand(prepareUserInput(args[0]+" "+args[1], args[2]));
            else
                throw new CommandNotFoundException();
            System.out.println("RESULT COMMAND -> "+result);
        } catch (CommandNotFoundException | InvalidCommandParametersException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Command to execute does NOT have parameters.
     * @param str Command, user input.
     * @return Returns result string.
     */
    private static CommandBuilder prepareUserInput(String str) {
        return new CommandBuilder(str, cmdUtils);
    }

    /**
     * Command to execute has parameters.
     * @param str Command, user input.
     * @return Returns result string.
     */
    private static CommandBuilder prepareUserInput(String str, String params) throws InvalidCommandParametersException {
        return new CommandBuilder(str, params, cmdUtils);
    }

    /**
     * Executes and validates the user command.
     *
     * @param cmdBuilder Builded command ready to be searched.
     * @return Returns the SQL string that should be queried to the SQL server.
     * @throws SQLException
     * @throws CommandNotFoundException
     * @throws InvalidCommandParametersException
     */
    private static String executeBuildedCommand(CommandBuilder cmdBuilder) throws SQLException, CommandNotFoundException, InvalidCommandParametersException {
        Connection con = null;
        String result;
        try {
            con = Sql.CreateConnetion();
            Command cmd = cmdUtils.getCmdTree().search(cmdBuilder);
            if (cmd == null)
                throw new CommandNotFoundException();

            //TODO: ATM ITS NOT QUERYING THE SQL SERVER
            //TODO: Update this method comment when that gets fixed.
            result = cmd.execute(cmdBuilder);

        } finally {
            if (con != null) {
                con.close();
            }
        }
        return result;
    }
}
