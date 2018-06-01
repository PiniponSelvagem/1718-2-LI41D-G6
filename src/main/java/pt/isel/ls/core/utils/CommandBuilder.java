package pt.isel.ls.core.utils;

import pt.isel.ls.core.common.commands.Command;
import pt.isel.ls.core.common.headers.HeadersAvailable;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.strings.CommandEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.*;

public class CommandBuilder {
    private CommandUtils cmdUtils;
    private static final String REGEX_DIGITS = ".*\\d+.*";

    private String methodName;
    private LinkedList<String> path = new LinkedList<>();
    private HashMap<String, LinkedList<String>> params;
    private HashMap<String, String> ids;
    private String headerType;
    private HashMap<String, String> headers;
    private Command buildedCommand;


    /**
     * Build new command from user input.
     * @param args Command string input.
     * @param cmdUtils CommandUtils
     * @throws CommandException CommandException
     */
    public CommandBuilder(String[] args, CommandUtils cmdUtils) throws CommandException {
        if (args.length == 0) throw new CommandException(COMMAND__NOT_FOUND);
        this.cmdUtils = cmdUtils;
        parseMethod(args);
        parsePath(args);
        findIdsAndReplace(this.cmdUtils);
        findOptions(args);
        buildCommand();
    }


    /**
     * Parse method name of this command.
     * @param args String[] containing the command
     */
    private void parseMethod(String[] args) {
        this.methodName = args[0];
    }

    /**
     * Parse path to list.
     * @param args String[] containing the command
     */
    private void parsePath(String[] args) throws CommandException {
        try {
            if (args.length >= 2 && args[1].subSequence(0, DIR_SEPARATOR.toString().length())
                    .equals(DIR_SEPARATOR.toString())) {
                this.path = pathToList(args[1]);
            } else {
                throw new CommandException(PATH__NOT_FOUND);
            }
        } catch (StringIndexOutOfBoundsException e) {
            //TODO: THIS IS JUST TEMPORARY TILL EXCEPTIONS REWORK.
            //TODO: NEXT EXAMPLE THROWS 2 EXCEPTIONS
            /*

            return null;

             */

            //TODO: IMPORTANT AND CHECK WHY IT GOES HERE IN RARE OCASIONS!
            /*
            EXAMPLE COMMAND THAT DOES THIS:
            return header.getBuildedString();
            }
            return null;

             */
            throw new CommandException(DEBUG__EXCEPTION);
        }
    }

    /**
     * Parse path of this command.
     * @param path Path string
     * @return LinkedList of String containing the path
     */
    public static LinkedList<String> pathToList(String path) {
        if (path != null) {
            LinkedList<String> pathList = new LinkedList<>();
            pathList.addAll(Arrays.asList(path.substring(1).split(DIR_SEPARATOR.toString())));
            return pathList;
        }
        return null;
    }

    /**
     * Checks for headers / parameters in the command args.
     * @param args String[] containing the command
     * @throws CommandException CommandException
     */
    private void findOptions(String[] args) throws CommandException {
        if (args.length == 3) {
            if (args[2] != null && args[2].contains(ACCEPT.toString())) {
                findHeaders(args[2]);
            }
            else {
                findParams(args[2]);
            }
        }
        else {
            if (args.length == 4) {
                findParams(args[2]);
                findHeaders(args[3]);
            }
        }
    }


    //TODO: small rework
    /**
     * Find, if present, the multiple IDs, save them and replace them with generic information.
     * @param cmdUtils CommandUtils
     */
    private void findIdsAndReplace(CommandUtils cmdUtils) {
        this.ids = new HashMap<>();
        String currDir = cmdUtils.getRootName();
        for (int i=0; i<path.size(); ++i) {
            if (path.get(i).length() > 0) {
                if (path.get(i).matches(REGEX_DIGITS)) {
                    ids.put(cmdUtils.getDirID(currDir), path.get(i));
                    path.set(i, cmdUtils.getDirID(currDir));
                }
            }
            currDir = path.get(i);
        }
    }

    /**
     * Find parameters and organize them for later use.
     * @param params String of the parameters to be worked with
     * @throws CommandException when parameters werent found
     */
    private void findParams(String params) throws CommandException {
        if (params == null) throw new CommandException(PARAMETERS__NOT_FOUND);
        this.params = new HashMap<>();
        String[] paramsSplit = params.split(PARAMS_SEPARATOR.toString());
        String value;
        LinkedList<String> list;

        for (String aParamsSplit : paramsSplit) {
            String[] aux = aParamsSplit.split(PARAMS_EQUALTO.toString());
            if (aux.length != 2) {
                throw new CommandException(PARAMETERS__NO_VALUE_ASSIGNED, aux[0]);
            }
            value = aux[1].replace(
                    PARAMS_VALS_SEPARATOR.toString(),
                    PARAMS_VALS_SEPERATOR_REPLACEMENT.toString()
            );

            list = this.params.get(aux[0]);
            if (list==null) list = new LinkedList<>();
            list.add(value);
            this.params.put(aux[0], list);
        }
    }

    /**
     * Find headers and organize them for later use.
     * @param headers String of headers to be workwd with
     * @throws CommandException when headers werent found
     */
    private void findHeaders(String headers) throws CommandException {
        if (headers == null) throw new CommandException(HEADERS__NOT_FOUND);
        this.headers = new HashMap<>();
        String[] headersSplit = headers.split(HEADERS_SEPERATOR.toString());

        for (String aHeadersSplit : headersSplit) {
            String[] aux = aHeadersSplit.split(HEADERS_EQUALTO.toString());
            if (aux.length != 2)
                throw new CommandException(HEADERS__NO_VALUE_ASSIGNED, aux[0]);
            this.headers.put(aux[0], aux[1]);
        }

        if (this.headers.containsKey(ACCEPT.toString())) {
            headerType = this.headers.get(ACCEPT.toString());
            if (!HeadersAvailable.contains(headerType))
                throw new CommandException(HEADERS__NOT_FOUND);
        }
        else
            throw new CommandException(HEADERS__INVALID);
    }

    /**
     * Throws CommandException if parameter wasnt found or if is not a known parameter.
     * Good to validate user input parameter, and stop the action if its not valid.
     * @param param Parameter to check
     * @throws CommandException when param is not valid nor found, telling the one it was checking
     */
    private void parameterValidator(CommandEnum param) throws CommandException {
        if (params == null || !params.containsKey(param.toString()) || !cmdUtils.validParam(param.toString()))
            throw new CommandException(PARAMETERS__EXPECTED, param.toString());
    }

    /**
     *  Search the header in the headers tree.
     */
    private void buildCommand() {
        buildedCommand = (Command) cmdUtils.getCmdTree().search(path, methodName);
    }



    /**
     * @return Returns this command METHOD.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @return Returns method path.
     */
    public LinkedList<String> getPath() {
        return path;
    }

    /**
     * Used when theres only one parameter for this key.
     * If its used when theres multiple parameters for same key (WHICH IT SHOULDNT, AND IF YOU DOING THAT ITS BAD PRACTICE!!!),
     * it will only return the first one.
     * @param param Parameter name.
     * @return Returns the desired parameter.
     * @throws CommandException check CommandException of {@link #parameterValidator(CommandEnum)}
     */
    public String getParameter(CommandEnum param) throws CommandException {
        parameterValidator(param);
        return params.get(param.toString()).getFirst();
    }

    /**
     * Used when theres multiple parameters with same key.
     * @param param Parameter key to get
     * @param i Index of the parameter in the list.
     * @return Returns the requested parameter value if valid, if indexoutofboundsexception returns null.
     * @throws CommandException check CommandException of {@link #parameterValidator(CommandEnum)}
     */
    public String getParameter(CommandEnum param, int i) throws CommandException {
        parameterValidator(param);
        return (i < getParameterSize(param)) ? params.get(param.toString()).get(i) : null;
    }

    /**
     * @param param Parameter key to get
     * @return Returns the list containing the multiple parameters with same key.
     * @throws CommandException check CommandException of {@link #parameterValidator(CommandEnum)}
     */
    public List<String> getParametersList(CommandEnum param) throws CommandException {
        parameterValidator(param);
        return params.get(param.toString());
    }

    /**
     * @param param Parameter key to check for
     * @return Returns the size of the list containing multiple parameters with same key.
     * @throws CommandException check CommandException of {@link #parameterValidator(CommandEnum)}
     */
    public int getParameterSize(CommandEnum param) throws CommandException {
        parameterValidator(param);
        return params.get(param.toString()).size();
    }

    /**
     * @param param Check if this param is present
     * @return Returns bool if params hashmap has this key.
     */
    public boolean hasParameter(CommandEnum param) {
        return params != null && params.containsKey(param.toString());
    }

    /**
     * Returns the id value for the requested key.
     * @param id "key" to get the value
     * @return Returns the corresponding to this id
     */
    public String getId(CommandEnum id) {
        return ids.get(ID_PREFIX+id.toString()+ID_SUFFIX);
    }

    /**
     * @return Returns the builded command.
     */
    public Command getCommand() {
        return buildedCommand;
    }

    /**
     * @return Returns String header type, if null return default one.
     */
    public String getHeaderType() {
        return (headerType == null) ? cmdUtils.defaultHeaderType : headerType;
    }
}
