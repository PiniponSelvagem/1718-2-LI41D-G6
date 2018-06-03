package pt.isel.ls.view.json;

import pt.isel.ls.core.common.commands.db_queries.SQLData;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.common.commands.db_queries.SQLData.PostDataEnum.PD_OK;
import static pt.isel.ls.core.utils.DataContainer.DataEnum.D_SQL;

public class PostView extends JsonView {

    public PostView(DataContainer data) {
        super(data);
    }

    @Override
    protected void createJson() {
        SQLData postData = (SQLData) data.getData(D_SQL);
        if (postData.getPdEnum() == PD_OK)
            json.addObject(
                    new String[]{"id"},
                    new String[]{postData.getId().toString()}
            );
        else
            json.addObject(
                    new String[]{},
                    new String[]{}
            );
    }
}
