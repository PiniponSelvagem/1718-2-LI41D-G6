package pt.isel.ls.view.command;

import pt.isel.ls.core.utils.DataContainer;

public abstract class CommandView {
    protected DataContainer data;

    /**
     * Prints to console all the information it has.
     */
    public abstract void printAllInfo();

    /**
     * Example usage:
     * CommandView cmdView = cmd.execute(...);
     * DataContainer data = cmdView.getData();
     * Cinema c = (Cinema) data.getData(0);    //value '0' is the first one, going to a max of 'data.size()'
     * System.out.println(c.getName());
     *
     * @return Returns DataContainer with all the information for this type of view.
     */
    public DataContainer getData() {
        return data;
    }
}
