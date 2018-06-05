package pt.isel.ls.view.console;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.CommandView;

public class ExitView extends CommandView {

    public ExitView(DataContainer data) {
        super(data);
    }

    @Override
    public String getString() {
        return "Terminating by user request...";
    }

    @Override
    public final void createView() {
        ;
    }
}
