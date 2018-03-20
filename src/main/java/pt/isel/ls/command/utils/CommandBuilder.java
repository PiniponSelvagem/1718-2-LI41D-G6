package pt.isel.ls.command.utils;

import pt.isel.ls.command.Command;
import pt.isel.ls.command.exceptions.InvalidCommandParametersException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class CommandBuilder {
    private String method;
    private LinkedList<String> path = new LinkedList<>();
    private HashMap<String, String> params;
    private Command cmd;
    private CommandUtils cmdUtils;
    private HashMap<String, String> ids;


    /**
     * Build new command.
     * @param str Command string syntax.
     * @param cmd Command {@link pt.isel.ls.command.Command}
     */
    public CommandBuilder(String str, Command cmd) {
        String[] command = str.split(String.valueOf(ARGS_SEPARATOR));
        stringToList(command);
        this.cmd = cmd;
    }


    /**
     * Build new command from user input.
     * @param args Command string input.
     * @param cmdUtils
     * @throws InvalidCommandParametersException
     */
    public CommandBuilder(String[] args, CommandUtils cmdUtils) throws InvalidCommandParametersException {
        this.cmdUtils = cmdUtils;
        stringToList(args);
        findIdsAndReplace(this.cmdUtils);
        if (args.length == 3)
            findParams(args[2]);
    }

    /**
     * Organizes the input command so it can be used.
     * @param cmdList Input command.
     */
    private void stringToList(String[] cmdList){
        if (cmdList.length == 0) {
            this.method = String.valueOf(HELP);
        } else {
            this.method = cmdList[0];
            if (cmdList.length >= 2)
                this.path.addAll(Arrays.asList(cmdList[1].substring(1).split(String.valueOf(DIR_SEPARATOR))));
        }
    }

    /**
     * Find, if present, the multiple IDs, save them and replace them with generic information.
     * @param cmdUtils
     */
    private void findIdsAndReplace(CommandUtils cmdUtils) {
        this.ids = new HashMap<>();
        String currDir = cmdUtils.getRootName();
        for (int i=0; i<path.size(); ++i) {
            if (Character.isDigit(path.get(i).charAt(0))) {
                ids.put(cmdUtils.getDirID(currDir), path.get(i));
                path.set(i, cmdUtils.getDirID(currDir));
            }
            currDir = path.get(i);
        }
    }

    /**
     * @return Returns this CommandUtils
     */
    public CommandUtils getCmdUtils() {
        return cmdUtils;
    }

    /**
     * Find parameters and organize them for later use.
     * @param params
     * @throws InvalidCommandParametersException
     */
    private void findParams(String params) throws InvalidCommandParametersException {
        if (params == null) throw new InvalidCommandParametersException();
        this.params = new HashMap<>();
        String[] paramsSplit = params.split(String.valueOf(PARAMS_SEPARATOR));

        for (String aParamsSplit : paramsSplit) {
            String[] aux = aParamsSplit.split(String.valueOf(PARAMS_EQUALTO));
            if (aux.length != 2)
                throw new InvalidCommandParametersException();
            this.params.put(aux[0], aux[1].replace(
                    String.valueOf(PARAMS_VALS_SEPARATOR), String.valueOf(PARAMS_VALS_SEPERATOR_REPLACEMENT)
            ));
        }
    }


    /**
     * @return Returns this command METHOD.
     */
    public String getMethod() {
        return method;
    }

    /**
     * @return Returns command path.
     */
    public LinkedList<String> getPath() {
        return path;
    }

    /**
     * Returns the desired parameter, if invalid throws an exception.
     * @param param Parameter name.
     * @return Returns the desired parameter.
     * @throws InvalidCommandParametersException
     */
    public String getParameter(String param) throws InvalidCommandParametersException {
        if (params == null || !params.containsKey(param) || !cmdUtils.validParam(param))
            throw new InvalidCommandParametersException();
        return params.get(param);
    }

    /**
     * @return Returns this command.
     */
    public Command getCommand() {
        return cmd;
    }

    //TODO: https://github.com/isel-leic-ls/1718-1-LI41D-G6/issues/4
    public String getId(String id) {
        return ids.get(ID_PREFIX+id+ID_SUFFIX);
    }
}
