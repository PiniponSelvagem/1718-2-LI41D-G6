package pt.isel.ls.command.utils;

import pt.isel.ls.command.*;


public class CommandTree {

    CommandNode root;
    CommandNode commonRoot;

    public CommandTree( CommandNode root ) {
        this.root = root;
        commonRoot = null;
    }

    public void add(CommandBuilder cmd) {
        root.add(cmd.getPath(), cmd.getMethod(), cmd.getCommand());
    }

    public Command search(CommandBuilder cmdBuilder) {
        return root.search(cmdBuilder);
    }
}
