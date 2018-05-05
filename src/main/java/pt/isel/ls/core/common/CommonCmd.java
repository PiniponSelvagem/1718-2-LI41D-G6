package pt.isel.ls.core.common;

public abstract class CommonCmd {

    /*
        This class is extended by Command and Header, since these classes are somewhat similar, they
        require the methods below.
     */

    /**
     * @return Returns method name of this command.
     */
    public abstract String getMethodName();

    /**
     * @return Returns the path to get to this command.
     */
    public abstract String getPath();
}
