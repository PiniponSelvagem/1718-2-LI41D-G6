package pt.isel.ls.view;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.apps.http_server.http.HttpStatusCode.METHOD_NOT_ALLOWED;
import static pt.isel.ls.core.common.headers.Html.h1;
import static pt.isel.ls.core.common.headers.Html.text;

public abstract class CommandView {
    protected String infoString;
    protected final DataContainer data;
    protected Header header;

    public CommandView(DataContainer data) {
        this.data = data;
    }

    /**
     * infoString -> information ready to be, for example printed to console.
     */
    private void allInfo() {
        header = data.getHeader();
        if (header != null) {
            if (header instanceof HtmlPage)   infoString = toHtml((HtmlPage) header);
            else if (header instanceof Plain) infoString = toPlain((Plain) header);
            else if (header instanceof Json)  infoString = toJson((Json) header);
        }
    }

    /**
     * Update header with builded string to be used as output.
     */
    public final void getAllInfoString() {
        if (infoString == null) {
            allInfo();
            data.updateHeader(header);
        }
    }

    /**
     * @return Returns String ready to use for Plain format
     */
    protected String toPlain(Plain header) {
        header.addTitle("INFO: Plain format for requested command not implemented.");
        return header.getBuildedString();
    }

    /**
     * @return Returns String ready to use for HTML format
     */
    protected String toHtml(HtmlPage header) {
        header.createPage(METHOD_NOT_ALLOWED, "NOT ALLOWED",
                h1(text("INFO: HTML format for requested command not implemented."))
        );
        return header.getBuildedString();
    }

    /**
     * @return Returns String ready to use for JSON format
     */
    protected String toJson(Json header) {
        header.addObject(new String[]{"info"}, new String[]{"JSON format for requested command not implemented."});
        return header.getBuildedString();
    }
}
