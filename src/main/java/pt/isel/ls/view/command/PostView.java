package pt.isel.ls.view.command;

public class PostView<T> extends CommandView {

    private T uniqueId;
    private String type;

    public PostView(String type, T uniqueId){
        this.type = type;
        this.uniqueId = uniqueId;
    }

    @Override
    protected void allInfo() {
        infoString = "Information posted with success." + System.lineSeparator()
                + type + " " + uniqueId;
    }
}
