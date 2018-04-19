package pt.isel.ls.core.utils;

import pt.isel.ls.core.commands.Command;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.headers.Header;
import pt.isel.ls.core.headers.Html;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static pt.isel.ls.core.strings.CommandEnum.*;
import static pt.isel.ls.core.strings.ExceptionEnum.*;

public class CommandBuilder {
    private Object method;
    private CommandUtils cmdUtils;

    private String methodName;
    private LinkedList<String> path = new LinkedList<>();
    private HashMap<String, String> params;
    private HashMap<String, String> ids;

    private String headerName;
    private LinkedList<String> pathHeaders;
    private HashMap<String, String> headers;
    private Header header;


    /**
     * Build new command.
     * @param path Path to method.
     * @param method Command/Header {@link Command} {@link Header}
     */
    public CommandBuilder(String methodName, String path, Object method) {
        this.methodName = methodName;
        pathToList(path);
        this.method = method;
    }


    /**
     * Build new command from user input.
     * @param args Command string input.
     * @param cmdUtils
     * @throws CommandException
     */
    public CommandBuilder(String[] args, CommandUtils cmdUtils) throws CommandException {
        this.cmdUtils = cmdUtils;
        parseMethod(args);
        parsePath(args);
        findIdsAndReplace(this.cmdUtils);
        findOptions(args);
    }


    /**
     * Parse path of this command.
     * @param path
     */
    private void pathToList(String path) {
        if (path != null)
            this.path.addAll(Arrays.asList(path.substring(1).split(String.valueOf(DIR_SEPARATOR))));
    }

    /**
     * Parse method name of this command.
     * @param args
     */
    private void parseMethod(String[] args) {
        this.methodName = args[0];
    }

    /**
     * Parse path to list.
     * @param args
     */
    private void parsePath(String[] args) {
        if (args.length >= 2) {
            pathToList(args[1]);
        }
    }

    /**
     * Checks for headers / parameters in the command args.
     * @param args
     * @throws CommandException
     */
    private void findOptions(String[] args) throws CommandException {
        /*
        FUTURE NOTE: Parameters for date with time style -> 12:55, can conflict with
                     detection of headers, since ':' is the equals syntax for it.
                     ATM to work around this, its looking for '=' syntax, since its
                     only used for the parameters equals syntax.

        WARNING:     UNTIL A FIX IS FOUND FOR THIS SITUATION, THE args[x].contains
                     MUST BE DIFFERENT THAN HEADERS_EQUALTO.
        */

        if (args.length == 3) {
            if (args[2].contains(String.valueOf(PARAMS_EQUALTO))) {
                findParams(args[2]);
            }
            else {
                findHeaders(args[2]);
            }
        }
        else {
            if (args.length == 4) {
                if (args[3].contains(String.valueOf(PARAMS_EQUALTO))) {
                    findParams(args[3]);
                    findHeaders(args[2]);
                }
                else {
                    findHeaders(args[3]);
                    findParams(args[2]);
                }
            }
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
            //if (cmdUtils.getDirID(currDir) != null && ) {
            char testChar;
            if (path.get(i).length() >= 2)
                testChar = path.get(i).charAt(1);
            else
                testChar = path.get(i).charAt(0);

            if (Character.isDigit(testChar)) {
                ids.put(cmdUtils.getDirID(currDir), path.get(i));
                path.set(i, cmdUtils.getDirID(currDir));
            }
            currDir = path.get(i);
        }
    }

    /**
     * Find parameters and organize them for later use.
     * @param params
     * @throws CommandException
     */
    private void findParams(String params) throws CommandException {
        if (params == null) throw new CommandException(PARAMETERS__NOT_FOUND);
        this.params = new HashMap<>();
        String[] paramsSplit = params.split(String.valueOf(PARAMS_SEPARATOR));

        for (String aParamsSplit : paramsSplit) {
            String[] aux = aParamsSplit.split(String.valueOf(PARAMS_EQUALTO));
            if (aux.length != 2) {
                throw new CommandException(String.format(String.valueOf(PARAMETERS__NO_VALUE_ASSIGNED), aux[0]));
            }
            this.params.put(aux[0], aux[1].replace(
                    String.valueOf(PARAMS_VALS_SEPARATOR), String.valueOf(PARAMS_VALS_SEPERATOR_REPLACEMENT)
            ));
        }
    }

    /**
     * Find headers and organize them for later use.
     * @param headers
     * @throws CommandException
     */
    private void findHeaders(String headers) throws CommandException {
        if (headers == null) throw new CommandException(HEADERS__NOT_FOUND);
        this.headers = new HashMap<>();
        String[] headersSplit = headers.split(String.valueOf(HEADERS_SEPERATOR));

        for (String aHeadersSplit : headersSplit) {
            String[] aux = aHeadersSplit.split(String.valueOf(HEADERS_EQUALTO));
            if (aux.length != 2)
                throw new CommandException(String.format(String.valueOf(HEADERS__NO_VALUE_ASSIGNED), aux[0]));
            this.headers.put(aux[0], aux[1]);
        }

        if (this.headers.containsKey(String.valueOf(ACCEPT))) {
            this.pathHeaders = new LinkedList<>();
            this.pathHeaders.addAll(Arrays.asList(this.headers.get(String.valueOf(ACCEPT)).split(String.valueOf(DIR_SEPARATOR))));
            headerName = pathHeaders.getLast();
            this.pathHeaders.removeLast();
        }
        else
            throw new CommandException(String.valueOf(HEADERS__INVALID));
    }


    /**
     * @return Returns this command METHOD.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @return Returns this command.
     */
    public Object getMethod() {
        return method;
    }

    /**
     * @return Returns method path.
     */
    public LinkedList<String> getPath() {
        return path;
    }

    /**
     * Returns the desired parameter, if invalid throws an exception.
     * @param param Parameter name.
     * @return Returns the desired parameter.
     * @throws CommandException
     */
    public String getParameter(String param) throws CommandException {
        if (params == null || !params.containsKey(param) || !cmdUtils.validParam(param))
            throw new CommandException(String.format(String.valueOf(PARAMETERS__EXPECTED), param));
        return params.get(param);
    }

    //TODO: add comment
    public boolean hasParameter(String param) {
        return params != null && params.containsKey(param);
    }

    /**
     * Returns the id value for the requested key.
     * @param id
     * @return
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
     * @return If the command that is being requested to execute dosent have headers,
     *         add default header "HTML". If it has, create the correct header and its options.
     *         Search the command in the commands tree and return it.
     */
    public Command execute() throws CommandException {
        if (headers != null) {
            try {
                header = (Header) cmdUtils.getHeadersTree().search(pathHeaders, headerName);
                if (header != null) {
                    header = header.getClass().newInstance();
                    header.fileName = headers.get(String.valueOf(FILE_NAME));
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
        return (Command) cmdUtils.getCmdTree().search(path, methodName);
    }
}
