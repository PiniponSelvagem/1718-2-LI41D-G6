package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.apps.http_server.http.HttpStatusCode;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.common.headers.Html.a;
import static pt.isel.ls.core.common.headers.Html.li;

public class Index extends ServerPage {

    @Override
    public String body() {
        page.createPage(HttpStatusCode.OK, "Cinemas Info",
                h1(text("Cinemas Info")),
                li(a("/cinemas/", "Cinemas")),
                li(a("/movies/", "Movies"))
        );
        return page.getBuildedString();
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.OK;
    }
}
