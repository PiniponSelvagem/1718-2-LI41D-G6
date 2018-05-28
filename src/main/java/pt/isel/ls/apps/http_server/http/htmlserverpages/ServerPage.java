package pt.isel.ls.apps.http_server.http.htmlserverpages;

import pt.isel.ls.apps.http_server.http.HttpStatusCode;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;

public abstract class ServerPage {

    HtmlPage page = new HtmlPage();

    public abstract String body();

    public abstract HttpStatusCode getStatus();
}
