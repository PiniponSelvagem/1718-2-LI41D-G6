package pt.isel.ls.view;

import pt.isel.ls.core.common.headers.Json;
import pt.isel.ls.core.common.headers.Plain;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.apps.http_server.http.HttpStatusCode.OK;
import static pt.isel.ls.core.common.headers.Html.h1;
import static pt.isel.ls.core.common.headers.Html.text;

public class DeleteView extends CommandView {

    public DeleteView(DataContainer data) {
        super(data);
    }

    @Override
    protected String toPlain(Plain header) {
        header.addTitle("Information deleted with success.");
        return header.getBuildedString();
    }

    @Override
    protected String toHtml(HtmlPage header) {
        header.createPage(OK, "Deleted",
                h1(text("INFO: Information deleted with success."))
        );
        return header.getBuildedString();
    }

    @Override
    protected String toJson(Json header) {
        header.addObject(new String[]{"info"}, new String[]{"Information deleted with success."});
        return header.getBuildedString();
    }
}