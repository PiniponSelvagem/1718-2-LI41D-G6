package pt.isel.ls.view.html;

import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.CommandView;

public abstract class HtmlView extends CommandView {
    private HtmlPage htmlPage;

    public HtmlView(DataContainer data) {
        super(data);
    }

    @Override
    public final void createView() {
        htmlPage = createPage();
    }

    protected abstract HtmlPage createPage();

    @Override
    public final String getString() {
        return htmlPage.getString();
    }

    /**
     * @return Returns http status code
     */
    public abstract HttpStatusCode getCode();
}
