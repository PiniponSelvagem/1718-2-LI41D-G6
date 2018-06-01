package pt.isel.ls.core.utils.directorytree;


import pt.isel.ls.core.common.commands.Command;
import pt.isel.ls.core.utils.CommandBuilder;

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
    public void add(Command cmd) {
        root.add(CommandBuilder.pathToList(cmd.getPath()), cmd.getMethodName(), cmd);
    }

    /**
     * @return Returns the root dir of the tree.
     */
    public DirectoryNode getRoot() {
        return root;
    }
}
