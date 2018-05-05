package pt.isel.ls.core.utils;

import pt.isel.ls.core.common.CommonCmd;
import pt.isel.ls.core.common.commands.Command;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.common.headers.Html;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.*;

public class CommandBuilder {
    private CommandUtils cmdUtils;

    private String methodName;
    private LinkedList<String> path = new LinkedList<>();
    private HashMap<String, LinkedList<String>> params;
    private HashMap<String, String> ids;
    private String headerName;
    private LinkedList<String> pathHeaders;
    private HashMap<String, String> headers;
    private Header header;
    private Command buildedCommand;


    /**
     * Build new command from user input.
     * @param args Command string input.
     * @param cmdUtils CommandUtils
     * @throws CommandException CommandException
     */
    public CommandBuilder(String[] args, CommandUtils cmdUtils) throws CommandException {
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
        if (args.length >= 2 && args[1].subSequence(0, DIR_SEPARATOR.toString().length())
                .equals(DIR_SEPARATOR.toString())) {
            this.path = pathToList(args[1]);
        }
        else {
            throw new CommandException(PATH__NOT_FOUND);
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
        /*
        FUTURE NOTE: Parameters for date with time style -> 12:55, can conflict with
                     detection of headers, since ':' is the equals syntax for it.
                     ATM to work around this, its looking for '=' syntax, since its
                     only used for the parameters equals syntax.

        NOTE:        THE args[x].contains MUST BE DIFFERENT THAN HEADERS_EQUALTO.
        */

        if (args.length == 3) {
            if (args[2].contains(PARAMS_EQUALTO.toString())) {
                findParams(args[2]);
            }
            else {
                findHeaders(args[2]);
            }
        }
        else {
            if (args.length == 4) {
                findHeaders(args[2]);
                findParams(args[3]);
            }
        }
    }



    /**
     * Find, if present, the multiple IDs, save them and replace them with generic information.
     * @param cmdUtils CommandUtils
     */
    private void findIdsAndReplace(CommandUtils cmdUtils) {
        this.ids = new HashMap<>();
        String currDir = cmdUtils.getRootName();
        for (int i=0; i<path.size(); ++i) {
            if (path.get(i).length() > 0) {
                //if (cmdUtils.getDirID(currDir) != null) {
                //TODO: this testChar and IfElse might be temporary till better solution is found.
                char testChar;
                if (path.get(i).length() >= 2)
                    testChar = path.get(i).charAt(1);
                else
                    testChar = path.get(i).charAt(0);

                if (Character.isDigit(testChar)) {
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
     * @throws CommandException when parameters werent found or no value was assigned to the param that is currently checking wasnt found
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
                throw new CommandException(String.format(PARAMETERS__NO_VALUE_ASSIGNED.toString(), aux[0]));
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
     * @throws CommandException when headers werent found or no value was assigned to the header that is currently checking wasnt found
     */
    private void findHeaders(String headers) throws CommandException {
        if (headers == null) throw new CommandException(HEADERS__NOT_FOUND);
        this.headers = new HashMap<>();
        String[] headersSplit = headers.split(HEADERS_SEPERATOR.toString());

        for (String aHeadersSplit : headersSplit) {
            String[] aux = aHeadersSplit.split(HEADERS_EQUALTO.toString());
            if (aux.length != 2)
                throw new CommandException(String.format(HEADERS__NO_VALUE_ASSIGNED.toString(), aux[0]));
            this.headers.put(aux[0], aux[1]);
        }

        if (this.headers.containsKey(ACCEPT.toString())) {
            this.pathHeaders = new LinkedList<>();
            this.pathHeaders.addAll(Arrays.asList(this.headers.get(ACCEPT.toString()).split(DIR_SEPARATOR.toString())));
            headerName = pathHeaders.getLast();
            this.pathHeaders.removeLast();
        }
        else
            throw new CommandException(HEADERS__INVALID.toString());
    }

    /**
     * Throws CommandException if parameter wasnt found or if is not a known parameter.
     * Good to validate user input parameter, and stop the action if its not valid.
     * @param param Parameter to check
     * @throws CommandException when param is not valid nor found, telling the one it was checking
     */
    private void parameterValidator(String param) throws CommandException {
        if (params == null || !params.containsKey(param) || !cmdUtils.validParam(param))
            throw new CommandException(String.format(PARAMETERS__EXPECTED.toString(), param));
    }

    /**
     *  If the command that is being requested to execute dosent have headers,
     *  add default header "HTML". If it has, create the correct header and its options.
     *  Search the command in the commands tree.
     */
    private void buildCommand() throws CommandException {
        if (headers != null) {
            try {
                header = (Header) cmdUtils.getHeadersTree().search(pathHeaders, headerName);
                if (header != null) {
                    header = header.getClass().newInstance();
                    header.fileName = headers.get(FILE_NAME.toString());
                }
                else {
                    throw new CommandException(HEADERS__INVALID);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("ERROR: THIS SHOULD NOT HAPPEN! UNABLE TO CREATE HEADER!");
                System.out.println("       Falling back to default header creation...");
                header = new Html();
            }
        }
        else {
            header = new Html();
        }
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
     * @throws CommandException check CommandException of {@link #parameterValidator(String)}
     */
    public String getParameter(String param) throws CommandException {
        parameterValidator(param);
        return params.get(param).getFirst();
    }

    /**
     * Used when theres multiple parameters with same key.
     * @param param Parameter key to get
     * @param i Index of the parameter in the list.
     * @return Returns the requested parameter value if valid, if indexoutofboundsexception returns null.
     * @throws CommandException check CommandException of {@link #parameterValidator(String)}
     */
    public String getParameter(String param, int i) throws CommandException {
        parameterValidator(param);
        return (i < getParameterSize(param)) ? params.get(param).get(i) : null;
    }

    /**
     * @param param Parameter key to check for
     * @return Returns the size of the list containing multiple parameters with same key.
     * @throws CommandException check CommandException of {@link #parameterValidator(String)}
     */
    public int getParameterSize(String param) throws CommandException {
        parameterValidator(param);
        return params.get(param).size();
    }

    /**
     * @param param Check if this param is present
     * @return Returns bool if params hashmap has this key.
     */
    public boolean hasParameter(String param) {
        return params != null && params.containsKey(param);
    }

    /**
     * Returns the id value for the requested key.
     * @param id "key" to get the value
     * @return Returns the corresponding to this id
     */
    public String getId(String id) {
        return ids.get(ID_PREFIX+id+ID_SUFFIX);
    }

    /**
     * @return Returns this command header.
     */
    public Header getHeader() {
        return header;
    }

    /**
     * @return Returns the builded command.
     */
    public Command getCommand() {
        return buildedCommand;
    }
}
