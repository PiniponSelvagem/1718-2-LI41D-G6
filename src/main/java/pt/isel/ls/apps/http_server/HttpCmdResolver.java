package pt.isel.ls.apps.http_server;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.apps.http_server.http.htmlserverpages.*;
import pt.isel.ls.apps.http_server.http.htmlserverpages.PagesUtils;
import pt.isel.ls.core.exceptions.CommandException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class HttpCmdResolver extends HttpServlet {

    private PagesUtils pageUtils = new PagesUtils();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("--New request was received --");
        System.out.println(req.getMethod());
        System.out.println(req.getRequestURI());
        System.out.println(req.getHeader("Accept"));
        System.out.println(req.getQueryString());

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));

        String respBody = "";

        try {
            ServerPage page = pageUtils.getPage(req.getRequestURI());
            if (page == null) {
                String[] urlOptions;
                if (req.getParameterNames().hasMoreElements()) {
                    urlOptions = new String[] {req.getMethod(), req.getRequestURI(), req.getQueryString()};
                }
                else {
                    urlOptions = new String[] {req.getMethod(), req.getRequestURI()};
                }
                CommandRequest cmdReq = new CommandRequest(urlOptions, false);
                if (cmdReq.getCmdView() != null)
                    respBody = cmdReq.getCmdView().getAllInfoString();
            }
            else {
                respBody = page.body();
            }

            if (req.getRequestURI().equals("/favicon.ico")) {
                resp.setStatus(204);
            }
            else {
                resp.setStatus(200);
            }
        } catch (CommandException e) {
            respBody = new NotFound().body();
            resp.setStatus(404);

        } finally {
            byte[] respBodyBytes = respBody.getBytes(utf8);
            resp.setContentLength(respBodyBytes.length);
            OutputStream os = resp.getOutputStream();
            os.write(respBodyBytes);
            os.close();
        }
    }
}
