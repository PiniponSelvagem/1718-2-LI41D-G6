package pt.isel.ls.view.command;

public class PostView extends CommandView {
    @Override
    public void printAllInfo() {
        System.out.println("Information posted with success.");
    }

    @Override
    public Object getSingle() {
        return null;
    }
}
