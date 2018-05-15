package pt.isel.ls.core.common.headers.html_utils;

import pt.isel.ls.core.common.headers.Html;
import pt.isel.ls.core.utils.writable.Writable;

public class HtmlPage extends Html {

    public HtmlPage(String title, Writable... c) {
        super(
                new HtmlElem("html",
                        new HtmlElem("head", new HtmlElem("title", new HtmlText(title))),
                        new HtmlElem("body", c)
                )
        );
    }

    //TODO: style should be more generic
    public HtmlPage(String title, String table, Writable... c) {
        super(
                new HtmlElem("html",
                        new HtmlElem("head",
                                new HtmlElem("title", new HtmlText(title)),
                                new HtmlElem("style", new HtmlText(styleMatrixTable(table)))),
                        new HtmlElem("body", c)
                )
        );
    }

    //TODO: dosent look pretty
    private static String styleMatrixTable(String tableName) {
        return ".divTable{display: table;}" +
                ".divTableRow{display: table-row;}" +
                ".divTableData{display: table-cell;}" +
                ".imgBracketTop{width: 14px; height: 7px;}" +
                ".imgBracketMiddle{width: 14px; height: 100%;}" +
                ".imgBracketBottom{width: 14px; height: 5px;}" +
                "."+tableName+"{border-collapse: collapse; text-align: center;}" +
                "."+tableName+"tableInnerMatrix td{width: 1.4em; height: 1.4em;}";
    }
}
