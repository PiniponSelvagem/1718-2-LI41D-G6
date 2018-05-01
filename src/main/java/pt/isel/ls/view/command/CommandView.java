package pt.isel.ls.view.command;

import pt.isel.ls.core.utils.DataContainer;

public abstract class CommandView {
    protected String infoString;
    protected DataContainer data;

    /**
     * @return String, information ready to be, for example printed to console.
     */
    protected abstract void allInfo();

    /**
     * @return String, information ready to be, for example printed to console.
     */
    public String getAllInfoString() {
        if (infoString == null) {
            allInfo();
        }
        return infoString;
    }

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
