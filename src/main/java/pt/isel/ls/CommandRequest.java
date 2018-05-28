package pt.isel.ls;

import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.exceptions.InvalidParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;
import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.CommandView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static pt.isel.ls.core.strings.ExceptionEnum.COMMAND__NOT_FOUND;
import static pt.isel.ls.core.strings.ExceptionEnum.VIEW_CREATION_ERROR;

public class CommandRequest {

    private CommandView cmdView;
    private DataContainer data;
    private final CommandUtils cmdUtils = Main.getCmdUtils();

    public CommandRequest(String[] args) throws CommandException, InvalidParameterException {
        commandRequest(args);
    }

    /**
     * Creates the CommandBuilder and creates the command making it ready to be executed.
     * Then it checks if the command requires a SQL connection and executes the command accordingly.
     * @param args {method, path, header, parameters} or {method, path, header, parameters}
     */
    private void commandRequest(String[] args) throws CommandException, InvalidParameterException {
        CommandBuilder cmdBuilder = new CommandBuilder(args, cmdUtils);
        if (cmdBuilder.getCommand() == null)
            throw new CommandException(COMMAND__NOT_FOUND);
        data = executeCommand(cmdBuilder);
    }

    /**
     * Execute the command
     * @param cmdBuilder CommandBuilder containing the input command ready to be executed
     * @return Returns the DataContainer with the info that the command got
     * @throws CommandException CommandException
     */
    private DataContainer executeCommand(CommandBuilder cmdBuilder) throws CommandException, InvalidParameterException {
        return cmdBuilder.getCommand().execute(cmdBuilder);
    }

    /**
     * Execute the appropriate view for the command that was executed.
     */
    public Header executeView() throws CommandException {

        //TODO: I dont like this code, but works for now
        HashMap viewMap = cmdUtils.getCmdViewMap();
        String viewLink = (String) viewMap.get(data.getCreatedBy());
        if (viewLink!=null) {
            Object obj;
            try {
                Class<?> klass = Class.forName(viewLink);
                Constructor<?> constructor = klass.getConstructor(DataContainer.class);
                obj = constructor.newInstance(data);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new CommandException(VIEW_CREATION_ERROR);
            }
            cmdView = (CommandView) obj;
        }
        //TODO: --- END ---


        if (cmdView != null)
            cmdView.getAllInfoString();

        return data.getHeader();
    }

    /**
     * @return DataContainer from executed command
     */
    public DataContainer getData() {
        return data;
    }
}
