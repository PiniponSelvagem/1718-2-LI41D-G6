package pt.isel.ls;

import pt.isel.ls.core.commands.Command;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.sql.Sql;
import pt.isel.ls.view.command.CommandView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static pt.isel.ls.core.strings.CommandEnum.ARGS_SEPARATOR;
import static pt.isel.ls.core.strings.ExceptionEnum.COMMAND__NOT_FOUND;

public class Main {
    private final static String FILE_NAME_WELCOME = "welcome_message",
                                WAIT_INPUT = "> ";
    private final static CommandUtils cmdUtils = new CommandUtils();
    private static boolean close = false;

    public static void main(String[] args) {
        if (args.length != 0)
            commandRequest(args);
        else
            interactiveMode();
    }

    /**
     * Wait for user input command and then run it.
     */
    private static void interactiveMode() {
        Scanner in = new Scanner(System.in);
        String[] args;
        printWelcomeMessage();
        do {
            System.out.print(WAIT_INPUT);
            args = in.nextLine().split(String.valueOf(ARGS_SEPARATOR));
            commandRequest(args);

        } while(!close);
    }

    /**
     * First phase of the command request.
     * Here it creates the CommandBuilder and creates the command making it ready to be executed.
     * @param args {method, path, header, parameters} or {method, path, header, parameters}
     */
    private static void commandRequest(String[] args) {
        try {
            commandRequest2ndPhase(new CommandBuilder(args, cmdUtils));
        } catch (CommandException  e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Second phase of the command request.
     * After the first phase that created the command based on the user input and made it ready for the command
     * to be executed, here it check if the command requires a SQL connection and executes the command accordingly.
     * @param cmdBuilder builded command
     * @throws CommandException CommandException
     */
    private static void commandRequest2ndPhase(CommandBuilder cmdBuilder) throws CommandException {
        Connection con = null;
        Command cmd = cmdBuilder.getCommand();
        if (cmd == null)
            throw new CommandException(COMMAND__NOT_FOUND);

        try {
            if (cmd.isSQLRequired()) {
                con = Sql.getConnection();
                con.setAutoCommit(false);
                executeCommand(cmdBuilder, con);
                con.commit();
            }
            else {
                executeCommand(cmdBuilder, null);
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
    }

    /**
     * Executes the command and prints the output.
     * @param cmdBuilder CommandBuilder containing the builded command to be executed.
     * @param con SQL connection, can be NULL for internal commands that dont require it.
     * @return Returns the CommandView in case its needed to debug the DATA that this command requested from: example the database.
     * @throws CommandException CommandException
     * @throws SQLException SQLException
     */
    public static CommandView executeCommand(CommandBuilder cmdBuilder, Connection con) throws CommandException, SQLException {
        CommandView cmdView = cmdBuilder.getCommand().execute(cmdBuilder, con);

        if (cmdView != null)
            cmdView.printAllInfo();

        return cmdView;
    }


    /**
     * Prints to console the content of welcome_message file.
     */
    private static void printWelcomeMessage() {
        try {
            File file = new File(Main.class.getClassLoader().getResource(FILE_NAME_WELCOME).getFile());

            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while(line != null) {
                System.out.println(line);
                line = in.readLine();
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("WARNING: ["+FILE_NAME_WELCOME+"] not found.");
        }
    }

    /**
     * When this method is called, changes the "close" variable to true so when in interactive mode knows when to stop.
     */
    public static void close() {
        close = true;
    }
}
