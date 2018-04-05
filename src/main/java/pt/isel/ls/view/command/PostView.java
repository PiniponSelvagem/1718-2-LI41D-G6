package pt.isel.ls.view.command;

public class PostView<T> extends CommandView {

    private T uniqueId;
    private String type;

    public PostView(String type, T uniqueId){
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
