package pt.isel.ls;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.exceptions.InvalidParameterException;
import pt.isel.ls.core.utils.CommandUtils;

import java.io.*;
import java.util.Scanner;

import static pt.isel.ls.core.strings.CommandEnum.ARGS_SEPARATOR;

public class Main {
    private final static String FILE_NAME_WELCOME = "welcome_message",
                                WAIT_INPUT = "> ",
                                dirFiles   = "header_files";
    private final static CommandUtils cmdUtils = new CommandUtils();
    private static boolean close = false;

    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                CommandRequest cmdReq = new CommandRequest(args);
                output(cmdReq.getData().getHeader().fileName, cmdReq.executeView().getBuildedString());
            } catch (CommandException | InvalidParameterException e) {
                System.out.println(e.getMessage());
            }
        }
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
        CommandRequest cmdReq;
        do {
            System.out.print(WAIT_INPUT);
            args = in.nextLine().split(ARGS_SEPARATOR.toString());
            try {
                cmdReq = new CommandRequest(args);
                output(cmdReq.getData().getHeader().fileName, cmdReq.executeView().getBuildedString());
            } catch (CommandException | InvalidParameterException e) {
                System.out.println(e.getMessage());
            }

        } while(!close);
    }

    /**
     * Prints to console the content of welcome_message file.
     */
    private static void printWelcomeMessage() {
        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(FILE_NAME_WELCOME);
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
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

    /**
     * @return Returns commandUtils in this context.
     */
    public static CommandUtils getCmdUtils() {
        return cmdUtils;
    }

    /**
     * Write builded text to file.
     */
    private static void output(String fileName, String output) {
        if (fileName != null) {
            BufferedWriter writer;
            try {
                File file = new File(dirFiles);
                //noinspection ResultOfMethodCallIgnored
                file.mkdir();   //create dirFiles directory if dosent exits
                String fileFullPath = file.getPath()+"/"+fileName;
                writer = new BufferedWriter(new FileWriter(fileFullPath));
                writer.write(output);
                writer.close();
                System.out.println("Output saved to: ../"+fileFullPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(output);
        }
    }
}
