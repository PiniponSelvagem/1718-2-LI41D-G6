package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;

public class FavIcon extends ServerPage {

    @Override
    protected HtmlPage page() {
        return new HtmlPage(String.valueOf(status().valueOf()));
    }

    @Override
    protected HttpStatusCode status() {
        return HttpStatusCode.NO_CONTENT;
    }
}
