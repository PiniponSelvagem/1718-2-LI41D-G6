package pt.isel.ls.apps.heroku;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.apps.http_server.HttpCmdResolver;
import pt.isel.ls.apps.http_server.HttpServer;
import pt.isel.ls.core.utils.CommandUtils;

import java.net.BindException;

import static pt.isel.ls.core.common.headers.HeadersAvailable.TEXT_HTML;
import static pt.isel.ls.sql.Sql.CreateConnetion;

public class HerokuServer {
    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

    /*
     * TCP port where to listen.
     * Standard port for HTTP is 80 but might be already in use
     */
    private static final int LISTEN_PORT = 8080;
    private static int listenPort;
    private static final CommandUtils cmdUtils = new CommandUtils(TEXT_HTML.toString());
    private static final PGSimpleDataSource ds = CreateConnetion();

    public static void main(String[] args) {

        System.setProperty("org.slf4j.simpleLogger.levelInBrackets", "true");

        try {
            run();
        } catch (BindException e) {
            log.error("Server port already in use: '{}'", listenPort);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void run() throws Exception {
        log.info("Starting HTTP server...");

        String portDef = System.getenv("PORT");
        listenPort = portDef != null ? Integer.valueOf(portDef) : LISTEN_PORT;
        Server server = new Server(listenPort);
        log.info("Listening on port '{}'", listenPort);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new HttpCmdResolver(cmdUtils, ds)), "/*");
        server.start();
        log.info("Server started");
        server.join();

        server.stop();
        log.info("Server stopped.");
    }
}
