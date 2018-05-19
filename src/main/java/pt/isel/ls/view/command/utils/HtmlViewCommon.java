package pt.isel.ls.view.command.utils;

import pt.isel.ls.core.utils.writable.Writable;

import static pt.isel.ls.core.common.headers.Html.*;

public class HtmlViewCommon {

    /**
     * Fills the html header with column names
     * @param columns Columns names
     * @return Returns th header to use for the table
     */
    public static Writable[] fillTableHeader(String[] columns) {
        Writable[] th = new Writable[columns.length];
        for (int i=0; i<columns.length; ++i) {
            th[i] = th(text(columns[i]));
        }
        return th;
    }
}
