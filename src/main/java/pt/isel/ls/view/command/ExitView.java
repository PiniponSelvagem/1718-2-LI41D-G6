package pt.isel.ls.view.command;

public class ExitView extends CommandView {

    @Override
    public void printAllInfo() {
        System.out.println("Terminating by user request...");
    }
}
