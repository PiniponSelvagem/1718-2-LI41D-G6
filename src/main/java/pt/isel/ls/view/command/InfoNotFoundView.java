package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.headers.Html.*;

public class InfoNotFoundView extends CommandView {

    public InfoNotFoundView(DataContainer data) {
        this.data = data;
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Requested information not found.");

        return header.getBuildedString();
    }

    @Override
    protected String toHtml(Html header) {
        header = new HtmlPage("Invalid Page",
                h1(text("OOPS! What happen???")),
                h3(text("Seems like the requested page exists, " +
                        "but the info required to display it wasn't found.")),
                h3(text("Did you change the url to come to this page? If not, please contact the admin.")),
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
