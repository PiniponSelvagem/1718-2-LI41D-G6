package pt.isel.ls.view.json;

import pt.isel.ls.core.common.headers.json.Json;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.CommandView;

public abstract class JsonView extends CommandView {
    protected Json json = new Json();

    public JsonView(DataContainer data) {
        super(data);
    }

    final void setInfoNotFound() {
        json.addObject(null, null);
    }

    @Override
    public final String getString() {
        return json.getString();
    }
}
