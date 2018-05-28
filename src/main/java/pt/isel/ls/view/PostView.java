package pt.isel.ls.view;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.apps.http_server.http.HttpStatusCode;
import pt.isel.ls.core.common.commands.db_queries.PostData;
import pt.isel.ls.core.common.headers.Json;
import pt.isel.ls.core.common.headers.Plain;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataEnum.PD_OK;
import static pt.isel.ls.core.common.headers.Html.a;
import static pt.isel.ls.core.common.headers.Html.h3;
import static pt.isel.ls.core.common.headers.Html.text;
import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_POST;

public abstract class PostView extends CommandView {

    /**
     * NOTE: Implementations of this PostView, require this constructor to be public
     *       so the {@link CommandRequest#executeView()} can initialize it.
     */
    PostView(DataContainer data) {
        super(data);
    }

    protected abstract void htmlRedirectHendle(HtmlPage header, PostData postData);

    @Override
    protected final String toPlain(Plain header) {
        PostData postData = (PostData) data.getData(D_POST);
        if (postData.getPdEnum() == PD_OK)
            header.addDetailed(postData.getPdType().toString(),
                    new String[]{"ID"},
                    new String[]{postData.getId().toString()}
            );
        else {
            String msg = "";
            if (postData.getErrorCode() != 0)
                msg = postData.getErrorCode() + " -> ";

            header.addDetailed("POST failed",
                    new String[]{"ERROR"},
                    new String[]{msg+postData.getMsg()}
            );
        }
        return header.getBuildedString();
    }

    @Override
    protected final String toHtml(HtmlPage header) {
        PostData postData = (PostData) data.getData(D_POST);
        switch (postData.getPdEnum()) {
            case PD_OK:
                htmlRedirectHendle(header, postData);
                break;
            case PD_FAILED:
                header.createPage(HttpStatusCode.BAD_REQUEST, data.getCreatedBy(),
                        h3(a(DIR_SEPARATOR.toString(), "Main page")),
                        h3(text("ERROR: "+postData.getErrorCode()+" -> "+postData.getMsg()))
                );
                break;
            case PD_DOSENT_EXIST:
                header.createPage(HttpStatusCode.BAD_REQUEST, postData.getPdType().toString(),
                        h3(a(DIR_SEPARATOR.toString(), "Main page")),
                        h3(text(postData.getMsg()))
                );
                break;
            default:
                header.createPage(HttpStatusCode.BAD_REQUEST, "ERROR",
                        h3(a(DIR_SEPARATOR.toString(), "Main page")),
                        h3(text("UNKNOWN ERROR"))
                );
        }

        return header.getBuildedString();
    }

    @Override
    protected final String toJson(Json header) {
        PostData postData = (PostData) data.getData(D_POST);
        if (postData.getPdEnum() == PD_OK)
            header.addObject(
                    new String[]{"id"},
                    new String[]{postData.getId().toString()}
            );
        else
            header.addObject(
                    new String[]{},
                    new String[]{}
            );
        return header.getBuildedString();
    }
}
