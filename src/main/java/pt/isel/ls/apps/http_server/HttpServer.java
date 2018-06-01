package pt.isel.ls.apps.http_server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import pt.isel.ls.core.exceptions.ParameterException;

import java.net.BindException;

import static pt.isel.ls.core.strings.ExceptionEnum.SERVER_PORT_ALREADY_IN_USE;

public class HttpServer {
    /*
     * TCP port where to listen.
     */
    private int listenPort;

    public HttpServer(int listenPort) throws ParameterException {
        this.listenPort = listenPort;
        try {
            run();
        } catch (BindException e) {
            throw new ParameterException(SERVER_PORT_ALREADY_IN_USE, String.valueOf(listenPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws Exception {
        System.out.println("Starting HTTP server...");
        Server server = new Server(listenPort);
        System.out.println("Listening on port " + listenPort);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new HttpCmdResolver()), "/*");
        server.start();
        System.out.println("Server started");
        System.in.read();
        server.stop();
        System.out.println("Server stopped.");
    }
}
