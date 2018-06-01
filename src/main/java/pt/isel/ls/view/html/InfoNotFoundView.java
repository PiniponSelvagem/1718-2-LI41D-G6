package pt.isel.ls.view.html;

import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.headers.html.Html.*;

public class InfoNotFoundView extends HtmlView {
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
    public HtmlPage createPage() {
        return new HtmlPage("Invalid Page",
                h1(text("OOPS! What happen???")),
                h3(text("Seems like the requested page exists, " +
                        "but the info required to display it wasn't found.")),
                h3(text("Did you change the url to come to this page? If not, please contact the admin.")),
                h3(text(msg)),
                h2(a("/", "Main page"))
        );
    }

    @Override
    public HttpStatusCode getCode() {
        return HttpStatusCode.NOT_FOUND;
    }
}
