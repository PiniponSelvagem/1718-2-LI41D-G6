package pt.isel.ls.core.utils.directorytree;

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
     * Add the command to the right place based on the PATH, when PATH is empty
     * then places the METHOD aka Command there.
     * @param path Path left till the right place to add the core.
     * @param method Method name to add.
     * @param obj Command to add or other type.
     */
    public void add(LinkedList<String> path, String method, Object obj) {
        if ( path.isEmpty() ) {
            if (!methods.containsKey(method))
                methods.put(method, obj);
        } else {
            DirectoryNode currentChild = new DirectoryNode(path.pop());
            int index = childs.indexOf( currentChild );
            if ( index == -1 ) {
                childs.add( currentChild );
                currentChild.add(path, method, obj);
            } else {
                DirectoryNode nextChild = childs.get(index);
                nextChild.add(path, method, obj);
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
    HashMap<String, Object> getMethods() {
        return methods;
    }
}
