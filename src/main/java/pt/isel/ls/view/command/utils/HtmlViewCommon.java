package pt.isel.ls.view.command.utils;

import pt.isel.ls.core.utils.writable.Writable;

import static pt.isel.ls.core.common.headers.Html.text;
import static pt.isel.ls.core.common.headers.Html.th;

public class HtmlViewCommon {

    public static Writable[] fillTableHeader(String[] columns) {
        Writable[] th = new Writable[columns.length];
        for (int i=0; i<columns.length; ++i) {
            th[i] = th(text(columns[i]));
        }
        return th;
    }

}
