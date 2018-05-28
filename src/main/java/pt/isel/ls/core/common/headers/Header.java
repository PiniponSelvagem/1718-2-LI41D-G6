package pt.isel.ls.core.common.headers;

import pt.isel.ls.core.common.CommonCmd;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;

public abstract class Header extends CommonCmd {
    protected StringBuilder text = new StringBuilder();
    public String fileName;

    /**
     * This constructor without parameters exists so {@link pt.isel.ls.core.utils.CommandUtils#headersTree}
     * can do {@link #getMethodName()} and {@link #getPath()} so later can be used to find the correct
     * header and create it.
     */
    public Header() {
        ;
    }

    /**
     * @return Returns buildedString.
     */
    public String getBuildedString() {
        return text.toString();
    }

    /**
     * @return Returns the path+methodname without the 1st "/",
     * while adding it between path and methodname.
     * example: text/html
     */
    public String getPathAndMethodName() {
        return getPath().substring(1)+DIR_SEPARATOR.toString()+getMethodName();
    }

    public Object getCode() {
        return null;
    }

    public String getRedirectLink() {
        return null;
    }
}
