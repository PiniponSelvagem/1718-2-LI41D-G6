package pt.isel.ls.view.command;

import java.util.LinkedList;

public abstract class CommandView {

    /**
     * Prints to console all the information it has.
     */
    public abstract void printAllInfo();

    /**
     * Example usage:
     *      CommandView cmdView = cmd.execute(...);
     *      LinkedList list = cmdView.getList();
     *      Cinema c = (Cinema) list.getFirst();
     *      System.out.println(c.getName());
     *
     * @return Returns LinkedList with all the information for this type of view.
     */
    public LinkedList getList() {
        return null;
    }

    /**
     * Example usage: (
     *      CommandView cmdView = cmd.execute(...);
     *      Cinema c = (Cinema) cmdView.getSingle();
     *      System.out.println(c.getName());
     *
     * @return Returns this view, better explanation:
     * Comparing to getList(), this returns 1 of those objects. If this view has a LinkedList to store objects,
     * this should return the first one.
     */
    public abstract Object getSingle();
}
