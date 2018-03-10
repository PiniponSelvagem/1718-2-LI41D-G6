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

    public CommandNode(String dir) {
        childs = new ArrayList<>();
        methods = new HashMap<>();
        this.dir = dir;
    }

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

    @Override
    public boolean equals(Object obj) {
        CommandNode cmpObj = (CommandNode)obj;
        return dir.equals( cmpObj.dir );
    }

    @Override
    public String toString() {
        return dir;
    }
}
