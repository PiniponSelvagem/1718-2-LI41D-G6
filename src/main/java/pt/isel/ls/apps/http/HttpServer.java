package pt.isel.ls.apps.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpServer {
    /*
     * TCP port where to listen.
     * Standard port for HTTP is 80 but might be already in use
     */
    private int listenPort;

    public HttpServer(int listenPort) throws Exception {
        this.listenPort = listenPort;
        run();
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
