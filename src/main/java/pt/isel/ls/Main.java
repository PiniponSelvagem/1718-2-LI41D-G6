package pt.isel.ls;

import pt.isel.ls.core.utils.CommandUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static pt.isel.ls.core.strings.CommandEnum.ARGS_SEPARATOR;

public class Main {
    private final static String FILE_NAME_WELCOME = "welcome_message",
                                WAIT_INPUT = "> ";
    private final static CommandUtils cmdUtils = new CommandUtils();
    private static boolean close = false;

    public static void main(String[] args) {
        if (args.length != 0)
            new CommandRequest(args, true);
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
            new CommandRequest(args, true);

        } while(!close);
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

    /**
     * @return Returns commandUtils in this context.
     */
    public static CommandUtils getCmdUtils() {
        return cmdUtils;
    }
}
