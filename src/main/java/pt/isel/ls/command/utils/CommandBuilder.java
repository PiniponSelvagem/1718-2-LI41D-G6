package pt.isel.ls.command.utils;

import pt.isel.ls.command.Command;

import java.util.Arrays;
import java.util.LinkedList;

public class CommandBuilder {
    private String method;
    private LinkedList<String> path = new LinkedList<>();
    private String params;
    private Command cmd;

    private LinkedList<Integer> ids;


    public CommandBuilder(String command, Command cmd) {
        stringToList(command);
        this.cmd = cmd;
    }

    public CommandBuilder(String command, CommandUtils cmdUtils) {
        stringToList(command);
        findIdsAndReplace(cmdUtils);
    }

    private void stringToList(String command){
        String[] cmdList = command.split(" ");
        this.method = cmdList[0];
        if (cmdList.length >= 2)
            this.path.addAll(Arrays.asList(cmdList[1].substring(1).split("/")));
    }

    private void findIdsAndReplace(CommandUtils cmdUtils) {
        this.ids = new LinkedList<>();
        String currDir = cmdUtils.getRootName();
        for (int i=0; i<path.size(); ++i) {
            if (Character.isDigit(path.get(i).charAt(0))) {
                ids.add(Integer.parseInt(path.get(i)));
                path.set(i, cmdUtils.getDirID(currDir));
            }
            currDir = path.get(i);
            //if (currDir==null) //TODO: [Exception] saying that "maybe you forgot to update initializeDirID hashmap"
        }
    }


    public String getMethod() {
        return method;
    }

    public LinkedList<String> getPath() {
        return path;
    }

    //TODO: parameters
    public String getParameters() {
        return "TO_BE_IMPLEMENTED";
    }

    public Command getCommand() {
        return cmd;
    }

    public int popId() {
        return ids.pop();
    }
}
