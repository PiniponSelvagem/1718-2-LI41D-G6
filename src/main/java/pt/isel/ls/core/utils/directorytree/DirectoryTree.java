package pt.isel.ls.core.utils.directorytree;


import pt.isel.ls.core.common.CommonCmd;
import pt.isel.ls.core.utils.CommandBuilder;

import java.util.LinkedList;

public class DirectoryTree {

    private DirectoryNode root;

    /**
     * Create new Tree.
     * @param root Base directory.
     */
    public DirectoryTree(DirectoryNode root) {
        this.root = root;
    }

    /**
     * @param cmd Add this object to the Tree.
     */
    public void add(CommonCmd cmd) {
        root.add(CommandBuilder.pathToList(cmd.getPath()), cmd.getMethodName(), cmd);
    }

    /**
     * Searches the object in the Tree.
     * @param path Search on this path.
     * @param name Search for this method/header/other with this name
     * @return Returns the requested core.
     */
    public Object search(LinkedList<String> path, String name) {
        return root.search(path, name);
    }
}
