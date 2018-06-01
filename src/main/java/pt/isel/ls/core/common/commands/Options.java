package pt.isel.ls.core.common.commands;

import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.DataContainer;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.OPTIONS;

public class Options extends Command {

    @Override
    public String getMethodName() {
        return OPTIONS.toString();
    }

    @Override
    public String getPath() {
        return DIR_SEPARATOR.toString();
    }

    @Override
    public DataContainer execute(CommandBuilder cmdBuilder) {
        return new DataContainer(this.getClass().getSimpleName());
    }
}
