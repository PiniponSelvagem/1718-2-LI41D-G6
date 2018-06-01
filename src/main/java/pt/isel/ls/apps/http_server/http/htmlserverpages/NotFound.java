package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;

import static pt.isel.ls.core.common.headers.html.Html.*;

public class NotFound extends ServerPage {

    //private final String msg;

    /*
    public NotFound(String msg) {
        this.msg = msg;
    }
    */

    @Override
    protected HtmlPage page() {
        return new HtmlPage(String.valueOf(status().valueOf()),
                h1(text("404: Page not found!")),
                h3(text("God damint Raminhos, not again...")),
                h3(text("*insert sad face*"))
                //h3(text(msg))
        );
    }

    @Override
    protected HttpStatusCode status() {
        return HttpStatusCode.NOT_FOUND;
    }
}
