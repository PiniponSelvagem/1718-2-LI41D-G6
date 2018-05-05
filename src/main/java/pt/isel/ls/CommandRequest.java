package pt.isel.ls;

import pt.isel.ls.core.common.commands.Command;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.sql.Sql;
import pt.isel.ls.view.command.CommandView;

import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.core.strings.ExceptionEnum.COMMAND__NOT_FOUND;

public class CommandRequest {

    private CommandView cmdView;

    public CommandRequest(String[] args, boolean printToConsole) {
        commandRequest(args, printToConsole);
    }

    /**
     * Creates the CommandBuilder and creates the command making it ready to be executed.
     * Then it checks if the command requires a SQL connection and executes the command accordingly.
     * @param args {method, path, header, parameters} or {method, path, header, parameters}
     */
    private void commandRequest(String[] args, boolean printToConsole) {
        try {
            Connection con = null;
            CommandBuilder cmdBuilder = new CommandBuilder(args, Main.getCmdUtils());
            Command cmd = cmdBuilder.getCommand();
            if (cmd == null)
                throw new CommandException(COMMAND__NOT_FOUND);

            try {
                if (cmd.isSQLRequired()) {
                    con = Sql.getConnection();
                    con.setAutoCommit(false);
                    executeCommand(cmdBuilder, con, printToConsole);
                    con.commit();
                }
                else {
                    executeCommand(cmdBuilder, null, printToConsole);
                }
            } catch (SQLException e) {
                //TODO: Find a better way to handle SQL exceptions!
                //if possible make it so a "fool" user can understand :D
                System.out.println("ERROR CODE: "+e.getErrorCode()+" -> "+e.getMessage());
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (CommandException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the command and prints the output.
     * @param cmdBuilder CommandBuilder containing the builded command to be executed.
     * @param con SQL connection, can be NULL for internal commands that dont require it.
     * @return Returns the CommandView in case its needed to debug the DATA that this command requested from: example the database.
     * @throws CommandException CommandException
     * @throws SQLException SQLException
     */
    public CommandView executeCommand(CommandBuilder cmdBuilder, Connection con, boolean printToConsole) throws CommandException, SQLException {
        cmdView = cmdBuilder.getCommand().execute(cmdBuilder, con);

        if (cmdView != null && printToConsole)
            System.out.println(cmdView.getAllInfoString());

        return cmdView;
    }

    /**
     * @return CommandView
     */
    public CommandView getCmdView() {
        return cmdView;
    }
}
