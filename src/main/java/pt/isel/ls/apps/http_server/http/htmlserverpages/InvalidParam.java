package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.apps.http_server.http.HttpStatusCode;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;

import static pt.isel.ls.core.common.headers.Html.*;

public class InvalidParam extends ServerPage {

    private final String msg;

    public InvalidParam(String msg) {
        this.msg = msg;
    }

    @Override
    public String body() {
        HtmlPage page = new HtmlPage();
        page.createPage(HttpStatusCode.BAD_REQUEST, "400",
                h1(text("Input invalid")),
                h3(text(msg)),
                h3(text("Suggestion: Check if the input was valid, and try again. " +
                        "If error persists and you are SURE that this is an error, contact the admin.")),
                h2(a("/", "Main page"))
        );
        return page.getBuildedString();
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_FOUND;
    }
}
