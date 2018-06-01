package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.core.common.headers.html.HttpResponse;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.common.headers.html.HtmlPage;

public abstract class ServerPage {

    protected abstract HtmlPage page();
    protected abstract HttpStatusCode status();

    public final HttpResponse getHttpResponse() {
        return new HttpResponse(status(), page().getString());
    }
}
