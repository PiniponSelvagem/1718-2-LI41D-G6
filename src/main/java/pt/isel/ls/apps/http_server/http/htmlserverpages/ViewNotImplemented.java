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
                h1(text("No Content")),
                h3(text("Request was valid and processed, but the html view to display it wasn't found.")),
                h3(text(msg))
        );
    }

    @Override
    protected HttpStatusCode status() {
        return HttpStatusCode.NO_CONTENT;
    }
}
