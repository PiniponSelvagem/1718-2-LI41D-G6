package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;

import static pt.isel.ls.core.common.headers.html.Html.*;

public class InvalidParam extends ServerPage {

    private final String msg;

    public InvalidParam(String msg) {
        this.msg = msg;
    }

    @Override
    protected HtmlPage page() {
        return new HtmlPage(String.valueOf(status().valueOf()),
                h1(text("Input invalid")),
                h3(text(msg)),
                h3(text("Suggestion: Check if the input was valid, and try again. " +
                        "If error persists and you are SURE that this is an error, contact the admin.")),
                h2(a("/", "Main page"))
        );
    }

    @Override
    protected HttpStatusCode status() {
        return HttpStatusCode.BAD_REQUEST;
    }
}
