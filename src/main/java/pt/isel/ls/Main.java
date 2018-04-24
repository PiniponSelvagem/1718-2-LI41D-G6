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
import static pt.isel.ls.core.strings.CommandEnum.EXIT;
import static pt.isel.ls.core.strings.ExceptionEnum.COMMAND__NOT_FOUND;

public class Main {
    private final static String FILE_NAME_WELCOME = "welcome_message",
                                WAIT_INPUT = "> ";
    private final static CommandUtils cmdUtils = new CommandUtils();

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

        } while(!args[0].equals(String.valueOf(EXIT)));
    }

    /**
     * Checks which type of command is being requested, internal or sql and prepares it.
     * @param args {method, path, header, parameters} or {method, path, header, parameters}
     */
    private static void commandRequest(String[] args) {
        try {
            executeCommand(prepareCommand(args));
        } catch (CommandException  e) {
            System.out.println(e.getMessage());
        }
    }

    //TODO: ADD COMMENT
    private static CommandBuilder prepareCommand(String[] args) throws CommandException {
        return new CommandBuilder(args, cmdUtils);
    }

    /**
     * External command request, opens an sql connection.
     * @param cmdBuilder builded command
     * @throws CommandException CommandException
     */
    private static void executeCommand(CommandBuilder cmdBuilder) throws CommandException {
        Connection con = null;

        Command cmd = cmdBuilder.buildCommand();
        if (cmd == null)
            throw new CommandException(COMMAND__NOT_FOUND);

        try {
            if (cmd.isSQLRequired()) {
                con = Sql.getConnection();
                con.setAutoCommit(false);
                executeView(cmdBuilder, con);
                con.commit();
            }
            else {
                executeView(cmdBuilder, null);
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

    //TODO: ADD COMMENT
    public static CommandView executeView(CommandBuilder cmdBuilder, Connection con) throws CommandException, SQLException {
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
}
