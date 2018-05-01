package pt.isel.ls.view.command;

public class InfoNotFoundView extends CommandView {

    @Override
    protected void allInfo() {
        infoString = "Requested information not found.";
    }
}
