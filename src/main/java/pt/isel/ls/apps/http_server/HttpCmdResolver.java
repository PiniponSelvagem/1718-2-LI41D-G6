package pt.isel.ls.apps.http_server;

import pt.isel.ls.CommandRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

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

        CommandRequest cmdReq = new CommandRequest(new String[]{req.getMethod(), req.getRequestURI()}, false);
        String respBody = "";
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
