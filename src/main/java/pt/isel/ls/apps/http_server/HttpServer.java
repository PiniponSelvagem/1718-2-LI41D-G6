package pt.isel.ls.apps.http_server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandUtils;

import java.net.BindException;

import static pt.isel.ls.core.strings.ExceptionEnum.SERVER_PORT_ALREADY_IN_USE;
import static pt.isel.ls.sql.Sql.CreateConnetion;

public class HttpServer {

    /*
     * TCP port where to listen.
     */
    private int listenPort;
    private CommandUtils cmdUtils;
    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);
    private static final PGSimpleDataSource ds = CreateConnetion();

    public HttpServer(int listenPort, CommandUtils cmdUtils) throws ParameterException {
        this.listenPort = listenPort;
        this.cmdUtils = cmdUtils;
        try {
            run();
        } catch (BindException e) {
            log.error("Server port already in use: '{}'", listenPort, this.hashCode());
            throw new ParameterException(SERVER_PORT_ALREADY_IN_USE, String.valueOf(listenPort));
        } catch (Exception e) {
            log.error(e.getMessage(), this.hashCode());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void run() throws Exception {
        log.info("Starting HTTP server...", this.hashCode());
        Server server = new Server(listenPort);
        log.info("Listening on port '{}'", listenPort, this.hashCode());
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new HttpCmdResolver(cmdUtils, ds)), "/*");
        server.start();
        log.info("Server started", this.hashCode());
        System.in.read();
        server.stop();
        log.info("Server stopped.", this.hashCode());
    }
}
