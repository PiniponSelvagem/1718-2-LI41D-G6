package pt.isel.ls.apps.http_server;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.core.common.headers.HeadersAvailable;
import pt.isel.ls.core.common.headers.html.HttpResponse;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.apps.http_server.http.htmlserverpages.*;
import pt.isel.ls.apps.http_server.http.htmlserverpages.PagesUtils;
import pt.isel.ls.core.exceptions.*;
import pt.isel.ls.view.CommandView;
import pt.isel.ls.view.html.HtmlView;
import pt.isel.ls.view.html.PostView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;

import static pt.isel.ls.core.common.headers.html.HttpStatusCode.OK;
import static pt.isel.ls.core.strings.CommandEnum.ACCEPT;
import static pt.isel.ls.core.strings.CommandEnum.HEADERS_EQUALTO;

public class HttpCmdResolver extends HttpServlet {
    private PagesUtils pageUtils = new PagesUtils();
    private static final String HDPRE = ACCEPT.toString()+HEADERS_EQUALTO.toString(), //header prefix
                                PLAIN = HeadersAvailable.TEXT_PLAIN.toString(),
                                JSON  = HeadersAvailable.APP_JSON.toString(),
                                HTML  = HeadersAvailable.TEXT_HTML.toString();

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
        if (!req.getMethod().equals("GET") && !req.getMethod().equals("POST")) {
            return new HttpResponse(HttpStatusCode.METHOD_NOT_ALLOWED);
        }

        ServerPage page = pageUtils.getPage(req.getRequestURI());

        if (page == null) {
            String[] urlOptions;
            try {
                CommandView cmdView;
                if (header.equals(HDPRE+HTML) && req.getMethod().equals("POST") && req.getParameterNames().hasMoreElements()) {
                    handleFormData(req);
                    urlOptions = new String[]{req.getMethod(), req.getRequestURI(), handleFormData(req), header};
                    cmdView = new CommandRequest(urlOptions).executeView();
                    if (cmdView instanceof PostView) {
                        String redirectLink = ((PostView) cmdView).getRedirectLink();
                        if (redirectLink != null)
                            return new HttpResponse(HttpStatusCode.SEE_OTHER)
                                    .withHeader("Location", redirectLink);
                    }
                } else if (req.getParameterNames().hasMoreElements()) {
                    urlOptions = new String[]{req.getMethod(), req.getRequestURI(), req.getQueryString(), header};
                } else {
                    urlOptions = new String[]{req.getMethod(), req.getRequestURI(), header};
                }
                cmdView = new CommandRequest(urlOptions).executeView();

                if (!(cmdView instanceof HtmlView))
                    return new HttpResponse(OK, cmdView.getString());  //in case the request was made with header != text/html
                return new HttpResponse(((HtmlView) cmdView).getCode(), cmdView.getString());

            } catch (CommandException e) {
                return new NotFound().getHttpResponse();
            } catch (ParameterException e) {
                return new InvalidParam(e.getMessage()).getHttpResponse();
            } catch (CommonException e) {
                return new HttpResponse(HttpStatusCode.NO_CONTENT);
            }
        }
        return page.getHttpResponse();
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
