package pt.isel.ls.view;

import pt.isel.ls.core.utils.DataContainer;

public abstract class CommandView {
    protected final DataContainer data;

    public CommandView(DataContainer data) {
        this.data = data;
    }

    /**
     * @return Returns String, basically the output.
     */
    public abstract String getString();
}
