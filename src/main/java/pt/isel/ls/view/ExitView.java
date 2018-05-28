package pt.isel.ls.view;

import pt.isel.ls.core.utils.DataContainer;

public class ExitView extends CommandView {

    public ExitView(DataContainer data) {
        super(data);
    }

    @Override
    protected void allInfo() {
        infoString = "Terminating by user request...";
    }
}
