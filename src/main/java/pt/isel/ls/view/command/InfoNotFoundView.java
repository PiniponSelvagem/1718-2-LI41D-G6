package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;

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
        return super.toHtml(header);
    }

    @Override
    protected String toJson(Json header) {
        header.addObject(
                null,
                null);

        return header.getBuildedString();
    }
}
