package pt.isel.ls.command.utils;

import pt.isel.ls.command.Command;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class CommandBuilder {
    private String method;
    private LinkedList<String> path = new LinkedList<>();
    private HashMap<String, String> params;
    private Command cmd;
    private CommandUtils cmdUtils;

    private LinkedList<Integer> ids;


    public CommandBuilder(String command, Command cmd) {
        stringToList(command);
        this.cmd = cmd;
    }

    public CommandBuilder(String command, String params, CommandUtils cmdUtils) throws InvalidCommandParametersException {
        this.cmdUtils = cmdUtils;
        stringToList(command);
        findIdsAndReplace(this.cmdUtils);
        findParams(params);
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
        }
    }

    private void findParams(String params) throws InvalidCommandParametersException {
        if (params == null) throw new InvalidCommandParametersException();

        this.params = new HashMap<>();
        String[] paramsSplit = params.split("&");

        for (String aParamsSplit : paramsSplit) {
            String[] aux = aParamsSplit.split("=");
            if (aux.length != 2)
                throw new InvalidCommandParametersException();
            this.params.put(aux[0], aux[1].replace("+", " "));
        }
    }


    public String getMethod() {
        return method;
    }

    public LinkedList<String> getPath() {
        return path;
    }

    public String getParameters(String param) throws InvalidCommandParametersException {
        if (!params.containsKey(param) || !cmdUtils.validParam(param))
            throw new InvalidCommandParametersException();
        return params.get(param);
    }

    public Command getCommand() {
        return cmd;
    }

    public int popId() {
        return ids.pop();
    }
}
