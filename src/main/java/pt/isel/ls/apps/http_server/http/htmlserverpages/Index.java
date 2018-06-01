package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.common.headers.html.Html.a;
import static pt.isel.ls.core.common.headers.html.Html.li;

public class Index extends ServerPage {

    @Override
    protected HtmlPage page() {
        return new HtmlPage("Cinemas Info",
                h1(text("Cinemas Info")),
                li(a("/cinemas/", "Cinemas")),
                li(a("/movies/", "Movies"))
        );
    }

    @Override
    protected HttpStatusCode status() {
        return HttpStatusCode.OK;
    }
}
