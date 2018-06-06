package pt.isel.ls.apps.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.exceptions.CommonException;
import pt.isel.ls.core.utils.CommandRequest;
import pt.isel.ls.core.utils.CommandUtils;

import java.io.*;
import java.util.Scanner;

import static pt.isel.ls.core.common.headers.HeadersAvailable.TEXT_HTML;
import static pt.isel.ls.core.strings.CommandEnum.ARGS_SEPARATOR;

public class Console {
    private final static Logger log = LoggerFactory.getLogger(Console.class);

    private final static String FILE_NAME_WELCOME = "welcome_message",
                                WAIT_INPUT = "> ",
                                dirFiles   = "header_files";
    private final static CommandUtils cmdUtils = new CommandUtils(TEXT_HTML.toString());
    private static boolean close = false;

    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                CommandRequest cmdReq = new CommandRequest(args, cmdUtils);
                cmdReq.checkAndExecuteCommand();
                output(cmdReq.getFileName(), cmdReq.executeView().getString());
            } catch (CommonException e) {
                log.error(e.getMessage());
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
                cmdReq = new CommandRequest(args, cmdUtils);
                cmdReq.checkAndExecuteCommand();
                output(cmdReq.getFileName(), cmdReq.executeView().getString());
            } catch (CommonException e) {
                log.error(e.getMessage());
            }

        } while(!close);
    }

    /**
     * Prints to console the content of welcome_message file.
     */
    private static void printWelcomeMessage() {
        try {
            InputStream inputStream = Console.class.getClassLoader().getResourceAsStream(FILE_NAME_WELCOME);
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = in.readLine();
            while(line != null) {
                System.out.println(line);
                line = in.readLine();
            }
        } catch (NullPointerException | IOException e) {
            log.warn("WARNING: ["+FILE_NAME_WELCOME+"] not found.");
        }
    }

    /**
     * When this method is called, changes the "close" variable to true so when in interactive mode knows when to stop.
     */
    public static void close() {
        close = true;
    }

    /**
     * Write builded text to file.
     */
    private static void output(String fileName, String output) {
        if (output != null) {
            if (fileName != null) {
                BufferedWriter writer;
                try {
                    File file = new File(dirFiles);
                    //noinspection ResultOfMethodCallIgnored
                    file.mkdir();   //create dirFiles directory if dosent exits
                    String fileFullPath = file.getPath() + "/" + fileName;
                    writer = new BufferedWriter(new FileWriter(fileFullPath));
                    writer.write(output);
                    writer.close();
                    System.out.println("Output saved to: ../" + fileFullPath);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            } else {
                System.out.println(output);
            }
        }
    }
}
