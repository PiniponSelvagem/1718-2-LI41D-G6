package pt.isel.ls.view.command;

public class InfoNotFoundView extends CommandView {

    @Override
    public void printAllInfo() {
        System.out.println("Requested information not found.");
    }
}
