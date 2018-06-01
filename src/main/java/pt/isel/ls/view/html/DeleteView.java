package pt.isel.ls.view.html;

import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.headers.html.Html.h1;
import static pt.isel.ls.core.common.headers.html.Html.text;

public class DeleteView extends HtmlView {

    public DeleteView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        return new HtmlPage("Deleted",
                h1(text("INFO: Information deleted with success."))
        );
    }

    @Override
    public HttpStatusCode getCode() {
        return HttpStatusCode.OK;
    }
}