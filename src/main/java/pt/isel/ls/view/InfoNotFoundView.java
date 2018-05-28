package pt.isel.ls.view;

import pt.isel.ls.apps.http_server.http.HttpStatusCode;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.headers.Html.*;

public class InfoNotFoundView extends CommandView {
    private final String msg;

    public InfoNotFoundView(DataContainer data) {
        super(data);
        this.msg = "";
    }

    public InfoNotFoundView(DataContainer data, String msg) {
        super(data);
        this.msg = msg;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Requested information not found.");
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(HtmlPage header) {
        header.createPage(HttpStatusCode.NOT_FOUND, "Invalid Page",
                h1(text("OOPS! What happen???")),
                h3(text("Seems like the requested page exists, " +
                        "but the info required to display it wasn't found.")),
                h3(text("Did you change the url to come to this page? If not, please contact the admin.")),
                h3(text(msg)),
                h2(a("/", "Main page"))
        );
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        header.addObject(
                null,
                null);
        return header.getBuildedString();
    }
}
