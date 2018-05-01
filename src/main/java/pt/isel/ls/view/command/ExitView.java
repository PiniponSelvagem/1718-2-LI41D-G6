package pt.isel.ls.view.command;

public class ExitView extends CommandView {

    @Override
    protected void allInfo() {
        infoString = "Terminating by user request...";
    }
}
