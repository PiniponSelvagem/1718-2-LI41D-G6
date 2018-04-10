package pt.isel.ls.core.utils.directorytree;

import pt.isel.ls.core.commands.NotFound;
import pt.isel.ls.core.utils.CommandBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DirectoryNode {

    private List<DirectoryNode> childs;
    private HashMap<String, Object> methods;
    private String dir;

    /**
     * CommandNode constructor, use {@link #add(LinkedList, String, Object)} (} instead to add a new node.
     * @param dir This directory.
     */
    public DirectoryNode(String dir) {
        childs = new ArrayList<>();
        methods = new HashMap<>();
        this.dir = dir;
    }

    /**
     * Add the core to the right place based on the PATH, when PATH is empty
     * then places the METHOD aka Command there.
     * @param path Path left till the right place to add the core.
     * @param method Method name to add.
     * @param cmd Command to add.
     */
    public void add(LinkedList<String> path, String method, Object cmd) {
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
     * Search for this command in this directory, not found? go to the next directory aka child.
     * @param path Search on this path.
     * @param methodName Method to search for.
     * @return If found return it, if not return {@link NotFound}
     */
    public Object search(LinkedList<String> path, String methodName) {
        if ( path.isEmpty() ) {
            return methods.get(methodName);
        } else {
            DirectoryNode currentChild = new DirectoryNode(path.pop());
            int index = childs.indexOf( currentChild );
            if ( index == -1 ) {
                return new NotFound();
            } else {
                DirectoryNode nextChild = childs.get(index);
                return nextChild.search(path, methodName);
            }
        }
    }

    /**
     * Compare if OBJ directory is equal to this one, this way there will not be duplicated directories.
     * This gets compared at line: int index = childs.indexOf( currentChild ); {@link #add(LinkedList, String, Object)}
     * @param obj Object to compare its directory with THIS one.
     * @return TRUE if its equal to this directory, FALSE if not and
     * at {@link #add(LinkedList, String, Object)} create a new directory.
     */
    @Override
    public boolean equals(Object obj) {
        DirectoryNode cmpObj = (DirectoryNode)obj;
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
