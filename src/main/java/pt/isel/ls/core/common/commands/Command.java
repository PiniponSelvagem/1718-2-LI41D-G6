package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.common.CommonCmd;
import pt.isel.ls.core.exceptions.CommandException;
import pt.isel.ls.core.exceptions.InvalidParameterException;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

public abstract class Command extends CommonCmd {

    /**
     * Executes the command and at the end returns DataContainer with information gathered
     *
     * @param cmdBuilder Command builder, aka context
     */
    public abstract DataContainer execute(CommandBuilder cmdBuilder) throws CommandException, InvalidParameterException;
}
