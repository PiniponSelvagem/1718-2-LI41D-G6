package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.exceptions.ParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

public abstract class Command {

    /**
     * @return Returns method name of this command.
     */
    public abstract String getMethodName();

    /**
     * @return Returns the path to get to this command.
     */
    public abstract String getPath();

    /**
     * Executes the command and at the end returns DataContainer with information gathered
     *
     * @param cmdBuilder Command builder, aka context
     */
    public abstract DataContainer execute(CommandBuilder cmdBuilder) throws CommandException, ParameterException;
}
