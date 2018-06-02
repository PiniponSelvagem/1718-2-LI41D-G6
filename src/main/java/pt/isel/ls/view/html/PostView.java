package pt.isel.ls.view.html;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.core.common.headers.html.HttpStatusCode;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.headers.html.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.headers.html.Html.*;
import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_POST;

public abstract class PostView extends HtmlView {
    protected PostData postData;
    private HttpStatusCode httpStatusCode;
    private String redirectLink;

    PostView(DataContainer data) {
        super(data);
    }

    @Override
    public HtmlPage createPage() {
        postData = (PostData) data.getData(D_POST);
        switch (postData.getPdEnum()) {
            case PD_OK:
                httpStatusCode = HttpStatusCode.CREATED;
                redirectLink = createRedirect();
            case PD_FAILED:
                httpStatusCode = HttpStatusCode.BAD_REQUEST;
                return new HtmlPage(data.getCreatedBy(),
                        h3(a(DIR_SEPARATOR.toString(), "Main page")),
                        h3(text("ERROR: "+postData.getErrorCode()+" -> "+postData.getMsg()))
                );
            case PD_DOSENT_EXIST:
                httpStatusCode = HttpStatusCode.BAD_REQUEST;
                return new HtmlPage(postData.getPdType().toString(),
                        h3(a(DIR_SEPARATOR.toString(), "Main page")),
                        h3(text(postData.getMsg()))
                );
        }

        httpStatusCode = HttpStatusCode.INTERNAL_SERVER_ERROR;
        return new HtmlPage("ERROR",
                h3(a(DIR_SEPARATOR.toString(), "Main page")),
                h3(text("UNKNOWN ERROR"))
        );
    }

    @Override
    public final HttpStatusCode getCode() {
        return httpStatusCode;
    }

    public abstract String createRedirect();

    public final String getRedirectLink() {
        return redirectLink;
    }
}
