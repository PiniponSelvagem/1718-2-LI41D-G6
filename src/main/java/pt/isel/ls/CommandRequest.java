package pt.isel.ls;

import pt.isel.ls.core.exceptions.*;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.CommandView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static pt.isel.ls.core.strings.ExceptionEnum.COMMAND__NOT_FOUND;
import static pt.isel.ls.core.strings.ExceptionEnum.VIEW__CREATION_ERROR;

public class CommandRequest {

    private CommandView cmdView;
    private CommandBuilder cmdBuilder;
    private DataContainer data;
    private final CommandUtils cmdUtils = Main.getCmdUtils();

    public CommandRequest(String[] args) throws CommonException {
        commandRequest(args);
    }

    /**
     * Creates the CommandBuilder and creates the command making it ready to be executed.
     * Then it checks if the command requires a SQL connection and executes the command accordingly.
     * @param args {method, path, header, parameters} or {method, path, header, parameters}
     */
    private void commandRequest(String[] args) throws CommonException {
        cmdBuilder = new CommandBuilder(args, cmdUtils);
        if (cmdBuilder.getCommand() == null)
            throw new CommandException(COMMAND__NOT_FOUND);
        data = executeCommand(cmdBuilder);
        data.headerType = cmdBuilder.getHeaderType();
    }

    /**
     * Execute the command
     * @param cmdBuilder CommandBuilder containing the input command ready to be executed
     * @return Returns the DataContainer with the info that the command got
     * @throws CommandException CommandException
     */
    private DataContainer executeCommand(CommandBuilder cmdBuilder) throws CommandException, ParameterException {
        return cmdBuilder.getCommand().execute(cmdBuilder);
    }


    /**
     * Create view for requested header
     * @return Returns CommandView ready for output.
     * @throws ViewNotImplementedException ViewNotImplementedException
     */
    public CommandView executeView() throws ViewNotImplementedException {
        HashMap<String, String> viewMap = cmdUtils.getCmdViewMap(data.headerType);

        if (viewMap == null)
            throw new ViewNotImplementedException(data.headerType);

        String viewLink = viewMap.get(data.getCreatedBy());
        if (viewLink!=null) {
            Object obj;
            try {
                Class<?> klass = Class.forName(viewLink);
                Constructor<?> constructor = klass.getConstructor(DataContainer.class);
                obj = constructor.newInstance(data);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new ViewNotImplementedException(VIEW__CREATION_ERROR);
            }
            cmdView = (CommandView) obj;
        }

        if (cmdView == null)
            throw new ViewNotImplementedException(data.headerType);

        return cmdView;
    }

    /**
     * @return DataContainer from executed command
     */
    public DataContainer getData() {
        return data;
    }

    /**
     * @return Returns fileName, can be null.
     */
    public String getFileName() {
        return cmdBuilder.getFileName();
    }
}
