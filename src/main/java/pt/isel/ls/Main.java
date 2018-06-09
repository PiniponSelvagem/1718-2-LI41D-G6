package pt.isel.ls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.apps.console.Console;
import pt.isel.ls.apps.heroku.HerokuServer;
import pt.isel.ls.apps.http_server.HttpServer;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);
    private static final String APP_MODE = "APP_MODE";

    public static void main(String[] args) {
        String appMode = System.getenv(APP_MODE);
        if (appMode != null && appMode.equals("heroku")) {
            log.info("Detected {} -> '{}', starting HEROKU MODE.", APP_MODE, appMode);
            HerokuServer.main(args);
        }
        else {
            log.info("No {} found, starting CONSOLE MODE.", APP_MODE);
            Console.main(args);
        }
    }
}
