package pt.isel.ls.command.utils;

import pt.isel.ls.command.*;


public class CommandTree {

    CommandNode root;
    CommandNode commonRoot;

    /**
     * Create new command Tree.
     * @param root Base directory.
     */
    public CommandTree( CommandNode root ) {
        this.root = root;
        commonRoot = null;
    }

    /**
     * @param cmd Add this command to the commands Tree.
     */
    public void add(CommandBuilder cmd) {
        root.add(cmd.getPath(), cmd.getMethod(), cmd.getCommand());
    }

    /**
     * Searches the command in the commands Tree.
     * @param cmdBuilder Search for this command, translated command.
     * @return Returns the requested command.
     */
    public Command search(CommandBuilder cmdBuilder) {
        return root.search(cmdBuilder);
    }
}
