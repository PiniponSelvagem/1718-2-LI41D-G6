package pt.isel.ls.apps.http_server;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.apps.http_server.http.htmlserverpages.*;
import pt.isel.ls.apps.http_server.http.htmlserverpages.PagesUtils;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.exceptions.CommandException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static pt.isel.ls.apps.http_server.http.HttpStatusCode.*;
import static pt.isel.ls.core.strings.CommandEnum.ACCEPT;
import static pt.isel.ls.core.strings.CommandEnum.HEADERS_EQUALTO;

public class HttpCmdResolver extends HttpServlet {
    private PagesUtils pageUtils = new PagesUtils();
    private static final String HDPRE = ACCEPT.toString()+HEADERS_EQUALTO.toString(), //header prefix
                                PLAIN = new Plain().getPathAndMethodName(),
                                JSON  = new Json().getPathAndMethodName(),
                                HTML  = new Html().getPathAndMethodName();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        /*
        System.out.println("--New request was received --");
        System.out.println(req.getMethod());
        System.out.println(req.getRequestURI());
        System.out.println(req.getHeader("Accept"));
        System.out.println(req.getQueryString());
        */

        String header = req.getHeader("Accept");
        if (header.contains(PLAIN))     header = HDPRE+PLAIN;
        else if (header.contains(JSON)) header = HDPRE+JSON;
        else header = HDPRE+HTML;

        //System.out.println(header);

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s", utf8.name()));

        String respBody = "";

        try {
            ServerPage page = pageUtils.getPage(req.getRequestURI());
            if (page == null) {
                String[] urlOptions;
                if (req.getParameterNames().hasMoreElements()) {
                    urlOptions = new String[] {req.getMethod(), req.getRequestURI(), req.getQueryString(), header};
                }
                else {
                    urlOptions = new String[] {req.getMethod(), req.getRequestURI(), header};
                }
                CommandRequest cmdReq = new CommandRequest(urlOptions, false);
                if (cmdReq.getCmdView() != null)
                    respBody = cmdReq.getCmdView().getAllInfoString();
            }
            else {
                respBody = page.body();
            }

            if (req.getRequestURI().equals("/favicon.ico")) {
                resp.setStatus(NO_CONTENT.valueOf());
            }
            else {
                resp.setStatus(OK.valueOf());
            }
        } catch (CommandException e) {
            respBody = new NotFound(e.getMessage()).body();
            resp.setStatus(NOT_FOUND.valueOf());

        } finally {
            byte[] respBodyBytes = respBody.getBytes(utf8);
            resp.setContentLength(respBodyBytes.length);
            OutputStream os = resp.getOutputStream();
            os.write(respBodyBytes);
            os.close();
        }
    }
}
