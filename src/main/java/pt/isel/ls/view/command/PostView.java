package pt.isel.ls.view.command;

public class PostView extends CommandView {

    private int uniqueId;
    private String type;

    public PostView(String type, int uniqueId){
        this.type = type;
        this.uniqueId = uniqueId;
    }

    @Override
    public void printAllInfo() {
        System.out.println("Information posted with success.");
        System.out.println(type + " " + uniqueId);
    }

    @Override
    public Object getSingle() {
        return null;
    }
}
