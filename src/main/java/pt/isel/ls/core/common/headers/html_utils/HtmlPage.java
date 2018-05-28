package pt.isel.ls.core.common.headers.html_utils;

import pt.isel.ls.apps.http_server.http.HttpStatusCode;
import pt.isel.ls.core.common.headers.Html;
import pt.isel.ls.core.utils.writable.Writable;

import static pt.isel.ls.apps.http_server.http.HttpStatusCode.SEE_OTHER;

public class HtmlPage extends Html {
    private HttpStatusCode respCode;
    private String redirectLink;
    public static final String COLOR_RED_LIGHT   = "#ff9999",
                               COLOR_GREEN_LIGHT = "#99ff99";

    /**
     * @return Returns http status code
     */
    public HttpStatusCode getCode() {
        return respCode;
    }

    public void createPage(HttpStatusCode respCode, String title, Writable... c) {
        this.respCode = respCode;
        _content = new HtmlElem("html",
                        new HtmlElem("head", new HtmlElem("title", new HtmlText(title))),
                        new HtmlElem("body", c)
                );
    }

    //TODO: style should be more generic
    public void createPageWithStyle_TicketTable(HttpStatusCode respCode, String title, String table, String submitButtonName, Writable... c) {
        this.respCode = respCode;
        _content = new HtmlElem("html",
                        new HtmlElem("head",
                                new HtmlElem("title", new HtmlText(title)),
                                new HtmlElem("style", new HtmlText(styleMatrixTable(table, submitButtonName)))),
                        new HtmlElem("body", c)
                );
    }

    public void createRedirect(String redirectLink) {
        respCode = SEE_OTHER;
        this.redirectLink = redirectLink;
    }

    @Override
    public String getRedirectLink() {
        return redirectLink;
    }

    //TODO: dosent look pretty
    private static String styleMatrixTable(String tableName, String submitButtonName) {
        return "."+tableName+"{border-collapse: collapse; text-align: center;}" +
                "."+tableName+"tableInnerMatrix td{width: 2em; height: 2em;}" +
                "."+submitButtonName+"{border: none; color: blue; margin-bottom: -14px; font-size: 16px; background-color: "+COLOR_GREEN_LIGHT+";};";
    }


}
