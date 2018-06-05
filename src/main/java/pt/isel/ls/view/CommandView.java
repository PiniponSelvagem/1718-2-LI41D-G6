package pt.isel.ls.view;

import pt.isel.ls.core.utils.DataContainer;

public abstract class CommandView {
    protected final DataContainer data;

    public CommandView(DataContainer data) {
        this.data = data;
    }

    /**
     * This method should be called after the constructor.
     * It creates the view and only then the {@link #getString()} can be called, or else it will return null.
     */
    public abstract void createView();

    /**
     * @return Returns String, basically the output.
     */
    public abstract String getString();
}
