package pt.isel.ls.command.utils;

import pt.isel.ls.command.Command;
import pt.isel.ls.command.NotFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CommandNode {

    private List<CommandNode> childs;
    private HashMap<String, Command> methods;
    private String dir;

    /**
     * CommandNode constructor, use {@link #add(LinkedList, String, Command)} (} instead.
     * @param dir This directory.
     */
    CommandNode(String dir) { //PACKAGE-PRIVATE
        childs = new ArrayList<>();
        methods = new HashMap<>();
        this.dir = dir;
    }

    /**
     * Add the command to the right place based on the PATH, when PATH is empty
     * then places the METHOD aka Command there.
     * @param path Path left till the right place to add the command.
     * @param method Method name to add.
     * @param cmd Command to add.
     */
    public void add(LinkedList<String> path, String method, Command cmd) {
        if ( path.isEmpty() ) {
            if (!methods.containsKey(method))
                methods.put(method, cmd);
        } else {
            CommandNode currentChild = new CommandNode(path.pop());
            int index = childs.indexOf( currentChild );
            if ( index == -1 ) {
                childs.add( currentChild );
                currentChild.add(path, method, cmd);
            } else {
                CommandNode nextChild = childs.get(index);
                nextChild.add(path, method, cmd);
            }
        }
    }

    /**
     * Search for this command in this directory, not found? go to the next directory aka child.
     * @param cmdBuilder Search for this command.
     * @return If found return it, if not return {@link pt.isel.ls.command.NotFound}
     */
    public Command search(CommandBuilder cmdBuilder) {
        if ( cmdBuilder.getPath().isEmpty() ) {
            return methods.get(cmdBuilder.getMethod());
        } else {
            CommandNode currentChild = new CommandNode(cmdBuilder.getPath().pop());
            int index = childs.indexOf( currentChild );
            if ( index == -1 ) {
                return new NotFound();
            } else {
                CommandNode nextChild = childs.get(index);
                return nextChild.search(cmdBuilder);
            }
        }
    }

    /**
     * Compare if OBJ directory is equal to this one, this way there will not be duplicated directories.
     * This gets compared at line: int index = childs.indexOf( currentChild ); {@link #add(LinkedList, String, Command)}
     * @param obj Object to compare its directory with THIS one.
     * @return TRUE if its equal to this directory, FALSE if not and
     * at {@link #add(LinkedList, String, Command)} create a new directory.
     */
    @Override
    public boolean equals(Object obj) {
        CommandNode cmpObj = (CommandNode)obj;
        return dir.equals( cmpObj.dir );
    }

    /**
     * @return Return this directory name.
     */
    @Override
    public String toString() {
        return dir;
    }
}