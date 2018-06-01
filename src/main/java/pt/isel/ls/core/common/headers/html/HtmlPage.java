package pt.isel.ls.core.common.headers.html;

import pt.isel.ls.core.utils.writable.Writable;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class HtmlPage extends Html {
    public static final String COLOR_RED_LIGHT   = "#ff9999",
                               COLOR_GREEN_LIGHT = "#99ff99";

    public HtmlPage(String title, Writable... c) {
        _content = new HtmlElem("html",
                new HtmlElem("head", new HtmlElem("title", new HtmlText(title))),
                new HtmlElem("body", c)
        );
    }

    //TODO: style should be more generic
    public HtmlPage(String title, String table, String submitButtonName, Writable... c) {
        _content = new HtmlElem("html",
                new HtmlElem("head",
                        new HtmlElem("title", new HtmlText(title)),
                        new HtmlElem("style", new HtmlText(styleMatrixTable(table, submitButtonName)))),
                new HtmlElem("body", c)
        );
    }

    @Override
    public String getString() {
        ByteArrayOutputStream _os = new ByteArrayOutputStream();
        Charset _charset = Charset.forName("UTF-8");
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_os, _charset))){
            if (_content!=null)
                _content.writeTo(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _os.toString();
    }

    //TODO: dosent look pretty
    private static String styleMatrixTable(String tableName, String submitButtonName) {
        return "."+tableName+"{border-collapse: collapse; text-align: center;}" +
                "."+tableName+"tableInnerMatrix td{width: 2em; height: 2em;}" +
                "."+submitButtonName+"{border: none; color: blue; margin-bottom: -14px; font-size: 16px; background-color: "+COLOR_GREEN_LIGHT+";};";
    }
}
