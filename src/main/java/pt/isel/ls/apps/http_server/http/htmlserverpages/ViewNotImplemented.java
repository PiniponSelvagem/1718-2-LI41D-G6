package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;

import static pt.isel.ls.core.common.headers.html.Html.*;

public class ViewNotImplemented extends ServerPage {

    private final String msg;

    public ViewNotImplemented(String msg) {
        this.msg = msg;
    }

    @Override
    protected HtmlPage page() {
        return new HtmlPage(String.valueOf(status().valueOf()),
                h1(text("204: NO CONTENT")),
                h3(text("Request was successful, but the page to display it is not implemented.")),
                h3(text(msg))
        );
    }

    @Override
    protected HttpStatusCode status() {
        return HttpStatusCode.NO_CONTENT;
    }
}
