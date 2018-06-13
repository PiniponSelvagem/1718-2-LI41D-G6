package pt.isel.ls.view.plain;

import pt.isel.ls.core.common.headers.plain.Plain;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.CommandView;

public abstract class PlainView extends CommandView {
    static final String infoNotFound = "Requested information not found.";
    protected Plain plain = new Plain();

    PlainView(DataContainer data) {
        super(data);
    }

    @Override
    public final String getString() {
        return plain.getString();
    }
}
