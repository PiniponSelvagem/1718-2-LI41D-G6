package pt.isel.ls.core.utils;

import pt.isel.ls.core.common.commands.Command;
import pt.isel.ls.core.common.headers.HeadersAvailable;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.exceptions.CommonException;
import pt.isel.ls.core.exceptions.HeaderException;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.strings.CommandEnum;
import pt.isel.ls.core.utils.directorytree.Router;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.*;

public class CommandBuilder {
    private CommandUtils cmdUtils;

    private String methodName;
    private LinkedList<String> path = new LinkedList<>();
    private HashMap<String, LinkedList<String>> params;
    private HashMap<String, String> ids;
    private String headerType, fileName;
    private Command buildedCommand;


    /**
     * Build new command from user input.
     * @param args Command string input.
     * @param cmdUtils CommandUtils
     * @throws CommandException CommandException
     */
    public CommandBuilder(String[] args, CommandUtils cmdUtils) throws CommonException {
        if (args.length == 0) throw new CommandException(COMMAND__NOT_FOUND);
        this.cmdUtils = cmdUtils;
        parseMethod(args);
        parsePath(args);
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
     * @throws ParameterException ParameterException
     * @throws HeaderException HeaderException
     */
    private void findOptions(String[] args) throws ParameterException, HeaderException {
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

    /**
     * Find parameters and organize them for later use.
     * @param params String of the parameters to be worked with
     * @throws ParameterException when parameters werent found
     */
    private void findParams(String params) throws ParameterException {
        if (params == null) throw new ParameterException(PARAMETERS__NOT_FOUND);
        this.params = new HashMap<>();
        String[] paramsSplit = params.split(PARAMS_SEPARATOR.toString());
        String value;
        LinkedList<String> list;

        for (String aParamsSplit : paramsSplit) {
            String[] aux = aParamsSplit.split(PARAMS_EQUALTO.toString());
            if (aux.length != 2) {
                throw new ParameterException(PARAMETERS__NO_VALUE_ASSIGNED, aux[0]);
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
     * @throws HeaderException when headers werent found
     */
    private void findHeaders(String headers) throws HeaderException {
        if (headers == null) throw new HeaderException(HEADERS__NOT_FOUND);
        HashMap<String, String> headersMap = new HashMap<>();
        String[] headersSplit = headers.split(HEADERS_SEPERATOR.toString());

        for (String aHeadersSplit : headersSplit) {
            String[] aux = aHeadersSplit.split(HEADERS_EQUALTO.toString());
            if (aux.length != 2)
                throw new HeaderException(HEADERS__NO_VALUE_ASSIGNED, aux[0]);
            headersMap.put(aux[0], aux[1]);
        }

        if (headersMap.containsKey(ACCEPT.toString())) {
            headerType = headersMap.get(ACCEPT.toString());
            if (!HeadersAvailable.contains(headerType))
                throw new HeaderException(HEADERS__NOT_FOUND);
        }
        else
            throw new HeaderException(HEADERS__INVALID);

        fileName = headersMap.get(FILE_NAME.toString());
    }

    /**
     * Throws CommandException if parameter wasnt found or if is not a known parameter.
     * Good to validate user input parameter, and stop the action if its not valid.
     * @param param Parameter to check
     * @throws ParameterException when param is not valid nor found, telling the one it was checking
     */
    private void parameterValidator(CommandEnum param) throws ParameterException {
        if (params == null || !params.containsKey(param.toString()) || !cmdUtils.validParam(param.toString()))
            throw new ParameterException(PARAMETERS__EXPECTED, param.toString());
    }

    /**
     *  Search for the command in the commands tree.
     */
    private void buildCommand() {
        buildedCommand = new Router(this).searchCommand();
    }



    /**
     * @return Returns cmdUtils
     */
    public CommandUtils getCmdUtils() {
        return cmdUtils;
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
     * @throws ParameterException check ParameterException of {@link #parameterValidator(CommandEnum)}
     */
    public String getParameter(CommandEnum param) throws ParameterException {
        parameterValidator(param);
        return params.get(param.toString()).getFirst();
    }

    /**
     * Used when theres multiple parameters with same key.
     * @param param Parameter key to get
     * @param i Index of the parameter in the list.
     * @return Returns the requested parameter value if valid, if indexoutofboundsexception returns null.
     * @throws ParameterException check ParameterException of {@link #parameterValidator(CommandEnum)}
     */
    public String getParameter(CommandEnum param, int i) throws ParameterException {
        parameterValidator(param);
        return (i < getParameterSize(param)) ? params.get(param.toString()).get(i) : null;
    }

    /**
     * @param param Parameter key to get
     * @return Returns the list containing the multiple parameters with same key.
     * @throws ParameterException check ParameterException of {@link #parameterValidator(CommandEnum)}
     */
    public List<String> getParametersList(CommandEnum param) throws ParameterException {
        parameterValidator(param);
        return params.get(param.toString());
    }

    /**
     * @param param Parameter key to check for
     * @return Returns the size of the list containing multiple parameters with same key.
     * @throws ParameterException check ParameterException of {@link #parameterValidator(CommandEnum)}
     */
    public int getParameterSize(CommandEnum param) throws ParameterException {
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
     * Set ids HashMap
     */
    public void setIds(HashMap<String, String> ids) {
        this.ids = ids;
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

    /**
     * @return Returns fileName, can be null.
     */
    public String getFileName() {
        return fileName;
    }
}
