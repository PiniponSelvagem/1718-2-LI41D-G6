package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;

import static pt.isel.ls.core.common.headers.html.Html.h1;
import static pt.isel.ls.core.common.headers.html.Html.text;

public class InternalServerError extends ServerPage {


    @Override
    protected HtmlPage page() {
        return new HtmlPage(String.valueOf(status().valueOf()),
                h1(text("500: Internal server error"))
        );
    }

    @Override
    protected HttpStatusCode status() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
