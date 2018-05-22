package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.core.common.headers.html_utils.HtmlPage;

import static pt.isel.ls.core.common.headers.Html.*;

public class NotFound extends ServerPage {

    private final String msg;

    public NotFound(String msg) {
        super();
        this.msg = msg;
    }

    @Override
    public String body() {
        return new HtmlPage("404",
                h1(text("404: Page not found!")),
                h3(text("God damint Raminhos, not again...")),
                h3(text("*insert sad face*")),
                h3(text(msg))
        ).getBuildedString();
    }
}
