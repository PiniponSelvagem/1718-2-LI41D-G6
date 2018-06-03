package pt.isel.ls.view.plain;

import pt.isel.ls.core.common.commands.db_queries.SQLData;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.commands.db_queries.SQLData.PostDataEnum.PD_OK;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SQL;

public class PostView extends PlainView {

    public PostView(DataContainer data) {
        super(data);
    }

    @Override
    protected void createPlain() {
        SQLData postData = (SQLData) data.getData(D_SQL);
        if (postData.getPdEnum() == PD_OK)
            plain.addDetailed(postData.getPdType().toString(),
                    new String[]{"ID"},
                    new String[]{postData.getId().toString()}
            );
        else {
            String msg = "";
            if (postData.getErrorCode() != 0)
                msg = postData.getErrorCode() + " -> ";

            plain.addDetailed("POST failed",
                    new String[]{"ERROR"},
                    new String[]{msg+postData.getMsg()}
            );
        }
    }
}
