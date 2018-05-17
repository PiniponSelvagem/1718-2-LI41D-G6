package pt.isel.ls.view.command;

public class PostView<T> extends CommandView {

    private final T uniqueId;
    private final String type;
    private final boolean success;

    public PostView(boolean success, String type, T uniqueId){
        this.success = success;
        this.type = type;
        this.uniqueId = uniqueId;
    }

    @Override
    protected void allInfo() {
        if (success)
            infoString = "Information posted with success." + System.lineSeparator() + type + " " + uniqueId;
        else
            infoString = "Information posted failed." + System.lineSeparator() + type + " " + uniqueId;
    }
}
