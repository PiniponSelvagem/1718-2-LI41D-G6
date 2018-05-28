package pt.isel.ls.apps.http_server;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.apps.http_server.http.HttpResponse;
import pt.isel.ls.apps.http_server.http.HttpStatusCode;
import pt.isel.ls.apps.http_server.http.htmlserverpages.*;
import pt.isel.ls.apps.http_server.http.htmlserverpages.PagesUtils;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.exceptions.InvalidParameterException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;

import static pt.isel.ls.apps.http_server.http.HttpStatusCode.*;
import static pt.isel.ls.core.strings.CommandEnum.ACCEPT;
import static pt.isel.ls.core.strings.CommandEnum.HEADERS_EQUALTO;

public class HttpCmdResolver extends HttpServlet {
    private PagesUtils pageUtils = new PagesUtils();
    private static final String HDPRE = ACCEPT.toString()+HEADERS_EQUALTO.toString(), //header prefix
                                PLAIN = new Plain().getPathAndMethodName(),
                                JSON  = new Json().getPathAndMethodName(),
                                HTML  = new HtmlPage().getPathAndMethodName();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doMethod(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doMethod(req, resp);
    }

    private void doMethod(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*
        System.out.println("--New request was received --");
        System.out.println(req.getMethod());
        System.out.println(req.getRequestURI());
        System.out.println(req.getHeader("Accept"));
        System.out.println(req.getQueryString());
        */

        //System.out.println(header);

        try {
            String header = req.getHeader("Accept");
            if (header.contains(PLAIN))     header = PLAIN;
            else if (header.contains(JSON)) header = JSON;
            else header = HTML;

            Charset utf8 = Charset.forName("utf-8");
            resp.setContentType(String.format(header+"; charset=%s", utf8.name()));
            header = HDPRE+header;

            handleRequest(header, req).send(resp);
        } catch (Throwable th) {
            // No exception should go unnoticed!
            new HttpResponse(HttpStatusCode.INTERNAL_SERVER_ERROR).send(resp);
        }
    }

    private HttpResponse handleRequest(String header, HttpServletRequest req) {
        ServerPage page = pageUtils.getPage(req.getRequestURI());
        HttpStatusCode code;

        if (page == null) {
            String[] urlOptions;
            try {
                if (req.getMethod().equals("POST") && req.getParameterNames().hasMoreElements()) {
                    handleFormData(req);
                    urlOptions = new String[]{req.getMethod(), req.getRequestURI(), handleFormData(req), header};
                    Header respHeader = new CommandRequest(urlOptions).executeView();
                    String redirectLink = respHeader.getRedirectLink();
                    if (redirectLink != null)
                        return new HttpResponse(HttpStatusCode.SEE_OTHER)
                                .withHeader("Location", respHeader.getRedirectLink());

                } else if (req.getParameterNames().hasMoreElements()) {
                    urlOptions = new String[]{req.getMethod(), req.getRequestURI(), req.getQueryString(), header};
                } else {
                    urlOptions = new String[]{req.getMethod(), req.getRequestURI(), header};
                }
                Header respHeader = new CommandRequest(urlOptions).executeView();

                if (respHeader.getCode() == null) code = OK;    //in case the request was made with header != text/html
                else code = (HttpStatusCode) respHeader.getCode();

                return new HttpResponse(code, respHeader.getBuildedString());
            } catch (CommandException e) {
                page = new NotFound(e.getMessage());
                return new HttpResponse(page.getStatus(), page.body());
            } catch (InvalidParameterException e) {
                page = new InvalidParam(e.getMessage());
                return new HttpResponse(page.getStatus(), page.body());
            }
        }
        return new HttpResponse(page.getStatus(), page.body());
    }

    private String handleFormData(HttpServletRequest req) {
        Enumeration<String> parameterNames = req.getParameterNames();
        StringBuilder str = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            String param = parameterNames.nextElement();
            str.append(param).append("=").append(req.getParameter(param).replace(" ", "+")).append("&");
        }
        str.deleteCharAt(str.length()-1); //remove last '&'
        return str.toString();
    }

}
