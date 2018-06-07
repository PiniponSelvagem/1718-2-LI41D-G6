package pt.isel.ls.apps.http_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.core.utils.CommandRequest;
import pt.isel.ls.core.common.headers.HeadersAvailable;
import pt.isel.ls.core.common.headers.html.HttpResponse;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.apps.http_server.http.htmlserverpages.*;
import pt.isel.ls.apps.http_server.http.htmlserverpages.PagesUtils;
import pt.isel.ls.core.exceptions.*;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.view.CommandView;
import pt.isel.ls.view.html.HtmlView;
import pt.isel.ls.view.html.PostView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Enumeration;

import static pt.isel.ls.core.common.headers.html.HttpStatusCode.OK;
import static pt.isel.ls.core.strings.CommandEnum.ACCEPT;
import static pt.isel.ls.core.strings.CommandEnum.HEADERS_EQUALTO;

public class HttpCmdResolver extends HttpServlet {
    private final static Logger log = LoggerFactory.getLogger(HttpCmdResolver.class);

    private CommandUtils cmdUtils;
    private PagesUtils pageUtils = new PagesUtils();
    private static final String HDPRE = ACCEPT.toString()+HEADERS_EQUALTO.toString(), //header prefix
                                PLAIN = HeadersAvailable.TEXT_PLAIN.toString(),
                                JSON  = HeadersAvailable.APP_JSON.toString(),
                                HTML  = HeadersAvailable.TEXT_HTML.toString();

    public HttpCmdResolver(CommandUtils cmdUtils) {
        this.cmdUtils = cmdUtils;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doMethod(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doMethod(req, resp);
    }

    private void doMethod(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String header = req.getHeader("Accept");
            if (header.contains(PLAIN))     header = PLAIN;
            else if (header.contains(JSON)) header = JSON;
            else header = HTML;

            Charset utf8 = Charset.forName("utf-8");
            resp.setContentType(String.format(header+"; charset=%s", utf8.name()));
            header = HDPRE+header;

            handleRequest(req, header).send(resp);
        } catch (Throwable th) {
            // No exception should go unnoticed!
            log.error("Internal server error.");
            th.printStackTrace();
            new InternalServerError().getHttpResponse();
        }
    }

    private HttpResponse handleRequest(HttpServletRequest req, String header) {
        if (!req.getMethod().equals("GET") && !req.getMethod().equals("POST")) {
            return new HttpResponse(HttpStatusCode.METHOD_NOT_ALLOWED);
        }

        ServerPage page = pageUtils.getPage(req.getRequestURI());

        if (page == null) {
            try {
                CommandView cmdView;
                if (header.equals(HDPRE+HTML) && req.getMethod().equals("POST") && req.getParameterNames().hasMoreElements()) {
                    cmdView = executeRequestPost(req, header);
                    if (cmdView instanceof PostView) {
                        String redirectLink = ((PostView) cmdView).getRedirectLink();
                        if (redirectLink != null)
                            return new HttpResponse(HttpStatusCode.SEE_OTHER)
                                    .withHeader("Location", redirectLink);
                    }
                } else {
                    cmdView = executeRequestGet(req, header);
                }

                if (!(cmdView instanceof HtmlView))
                    return new HttpResponse(OK, cmdView.getString());  //in case the request was made with header != text/html
                return new HttpResponse(((HtmlView) cmdView).getCode(), cmdView.getString());

            } catch (CommandException e) {
                return new NotFound().getHttpResponse();
            } catch (ParameterException e) {
                return new InvalidParam(e.getMessage()).getHttpResponse();
            } catch (ViewNotImplementedException e) {
                return new ViewNotImplemented(e.getMessage()).getHttpResponse();
            } catch (CommonException e) {
                return new HttpResponse(HttpStatusCode.NO_CONTENT);
            }
        }
        return page.getHttpResponse();
    }

    /**
     * Execute GET request and return the view for that commandrequest
     * @param req HttpServletRequest
     * @param header String, example: "accept:text/html"
     * @return CommandView
     * @throws CommonException CommonException
     */
    private CommandView executeRequestGet(HttpServletRequest req, String header) throws CommonException {
        String urlOptions[];
        CommandRequest cmdReq;
        CommandView cmdView;
        if (req.getParameterNames().hasMoreElements()) {
            urlOptions = new String[]{req.getMethod(), req.getRequestURI(), header, req.getQueryString()};

        } else {
            urlOptions = new String[]{req.getMethod(), req.getRequestURI(), header};
        }
        cmdReq = new CommandRequest(urlOptions, cmdUtils);
        cmdReq.checkAndExecuteCommand();
        cmdView = cmdReq.executeView();
        return cmdView;
    }

    /**
     * Execute POST request and return the view for that commandrequest
     * @param req HttpServletRequest
     * @param header String, example: "accept:text/html"
     * @return CommandView
     * @throws CommonException CommonException
     */
    private CommandView executeRequestPost(HttpServletRequest req, String header) throws CommonException {
        handleFormData(req);
        String urlOptions[] = new String[]{req.getMethod(), req.getRequestURI(), header, handleFormData(req)};
        CommandRequest cmdReq;
        CommandView cmdView;
        cmdReq = new CommandRequest(urlOptions, cmdUtils);
        cmdReq.checkAndExecuteCommand();
        cmdView = cmdReq.executeView();
        return cmdView;
    }

    /**
     * Builds the string fro parameters from the multiple parameters.
     * The params come in a Enumeration<String> and we need something like: "name=Cinema&city=Lisbon"
     * @param req HttpServletRequest
     * @return String example: "name=Cinema&city=Lisbon"
     */
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
