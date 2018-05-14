package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.core.common.headers.html_utils.HtmlPage;

import static pt.isel.ls.core.common.headers.Html.*;
import static pt.isel.ls.core.common.headers.Html.a;
import static pt.isel.ls.core.common.headers.Html.li;

public class Index extends ServerPage {

    @Override
    public String body() {
        return new HtmlPage("Cinemas Info",
                h1(text("Cinemas Info")),
                li(a("/cinemas/", "Cinemas")),
                li(a("/movies/", "Movies"))
        ).getBuildedString();
    }
}
