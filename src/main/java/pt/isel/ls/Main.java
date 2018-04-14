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

    public static void main(String[] args) {
        if (args.length != 0)
            commandRequest(args);
        else
            interactiveMode();
    }

    //TODO: add comment
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

    //TODO: add comment
    private static void commandRequest(String[] args) {
        try {
            if (args.length <= 1) {
                executeInternalCommand(args);
            }
            else if (args.length <= 4) {
                executeSQLCommand(args);
            }
            else
                throw new CommandException(COMMAND__NOT_FOUND);
        } catch (CommandException  e) {
            System.out.println(e.getMessage());
        }
    }

    //TODO: add comment
    private static void executeInternalCommand(String[] args) throws CommandException {
        executeBuildedCommand(new CommandBuilder(args, new CommandUtils())).printAllInfo();
    }

    //TODO: add comment
    private static void executeSQLCommand(String[] args) throws CommandException {
        Connection con = null;
        try {
            con = Sql.getConnection();
            con.setAutoCommit(false);
            executeBuildedCommand(con, new CommandBuilder(args, new CommandUtils())).printAllInfo();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
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
     * Executes and validates the user core, ONLY INTERNAL COMMANDS.
     * @param cmdBuilder
     * @return Returns the view for that core.
     */
    private static CommandView executeBuildedCommand(CommandBuilder cmdBuilder) throws CommandException {
        Command cmd = cmdBuilder.execute();
        if (cmd == null)
            throw new CommandException(COMMAND__NOT_FOUND);
        return cmd.execute(cmdBuilder);
    }

    /**
     * Executes and validates the user core.
     *
     * @param cmdBuilder Builded core ready to be searched.
     * @return Returns the view for that core.
     * @throws SQLException
     * @throws CommandException
     */
    public static CommandView executeBuildedCommand(Connection con, CommandBuilder cmdBuilder) throws CommandException {
        CommandView cmdView = null;

        try {
            Command cmd = cmdBuilder.execute();
            if (cmd == null)
                throw new CommandException(COMMAND__NOT_FOUND);
            cmdView = cmd.execute(cmdBuilder, con);

        } catch(SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return cmdView;
    }


    //TODO: add comment
    private static void printWelcomeMessage() {
        try {
            File file = new File(Main.class.getClassLoader().getResource(FILE_NAME_WELCOME).getFile());

            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while(line != null) {
                System.out.println(line);
                line = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("WARNING: ["+FILE_NAME_WELCOME+"] not found.");
        }
    }
}
