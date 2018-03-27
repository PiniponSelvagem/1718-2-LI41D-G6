package pt.isel.ls.view.command;

public class DeleteView extends CommandView {

    private String type;

    public DeleteView(String type){
        this.type = type;
    }

    @Override
    public void printAllInfo() {
        System.out.println("Information delete with success.");
        System.out.println(type);
    }

    @Override
    public Object getSingle() {
        return null;
    }
}