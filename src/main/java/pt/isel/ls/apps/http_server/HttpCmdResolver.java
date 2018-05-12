package pt.isel.ls.apps.http_server;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static pt.isel.ls.core.common.headers.Html.*;

public class HttpCmdResolver extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("--New request was received --");
        System.out.println(req.getMethod());
        System.out.println(req.getRequestURI());
        System.out.println(req.getHeader("Accept"));
        //System.out.println(req.getParameter("city"));

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));

        String respBody = "";
        if (req.getRequestURI().equals("/")) {
            respBody = new HtmlPage("Cinemas Info",
                    h1(text("Cinemas Info")),
                    li(a("/cinemas/", "Cinemas")),
                    li(a("/movies/", "Movies"))
            ).getBuildedString();
        }
        CommandRequest cmdReq = new CommandRequest(new String[]{req.getMethod(), req.getRequestURI()}, false);
        if (cmdReq.getCmdView() != null)
            respBody = cmdReq.getCmdView().getAllInfoString();

        byte[] respBodyBytes = respBody.getBytes(utf8);

        if (req.getRequestURI().equals("/favicon.ico"))
            resp.setStatus(204);
        else
            resp.setStatus(200);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }
}
