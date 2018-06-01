package pt.isel.ls.core.utils.directorytree;

import pt.isel.ls.core.common.commands.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DirectoryNode {

    private List<DirectoryNode> childs;
    private HashMap<String, Command> methods;
    private String dir;

    /**
     * CommandNode constructor, use {@link #add(LinkedList, String, Command)} (} instead to add a new node.
     * @param dir This directory.
     */
    public DirectoryNode(String dir) {
        childs = new ArrayList<>();
        methods = new HashMap<>();
        this.dir = dir;
    }

    /**
     * Add the command to the right place based on the PATH, when PATH is empty
     * then places the METHOD aka Command there.
     * @param path Path left till the right place to add the core.
     * @param method Method name to add.
     * @param cmd Command to add.
     */
    public void add(LinkedList<String> path, String method, Command cmd) {
        if ( path.isEmpty() ) {
            if (!methods.containsKey(method))
                methods.put(method, cmd);
        } else {
            DirectoryNode currentChild = new DirectoryNode(path.pop());
            int index = childs.indexOf( currentChild );
            if ( index == -1 ) {
                childs.add( currentChild );
                currentChild.add(path, method, cmd);
            } else {
                DirectoryNode nextChild = childs.get(index);
                nextChild.add(path, method, cmd);
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
        DirectoryNode cmpObj = (DirectoryNode) obj;
        return dir != null && dir.equals(cmpObj.dir);
    }

    /**
     * @return Return this directory name.
     */
    @Override
    public String toString() {
        return dir;
    }


    /** METHODS USED BY {@link Router} **/
    String getDir() {
        return dir;
    }
    List<DirectoryNode> getChilds() {
        return childs;
    }
    boolean childsContains(String dir) {
        return childs.contains(new DirectoryNode(dir));
    }
    HashMap<String, Command> getMethods() {
        return methods;
    }
}
