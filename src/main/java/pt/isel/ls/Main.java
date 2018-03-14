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
            if (args.length == 2)
                executeBuildedCommand(prepareUserInput(args[0] + " " + args[1]));
            else if (args.length == 3)
                executeBuildedCommand(prepareUserInput(args[0] + " " + args[1], args[2]));
            else
                throw new CommandNotFoundException();
        } catch (CommandNotFoundException | InvalidCommandParametersException e) {
            System.out.println(e.getMessage());
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
    private static void executeBuildedCommand(CommandBuilder cmdBuilder) throws CommandNotFoundException, InvalidCommandParametersException {
        Connection con = null;
        try {
            con = Sql.CreateConnetion();
            con.setAutoCommit(false);
            Command cmd = cmdUtils.getCmdTree().search(cmdBuilder);
            if (cmd == null)
                throw new CommandNotFoundException();

            cmd.execute(cmdBuilder, con);

        }catch(SQLException e){
            System.out.println(e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
