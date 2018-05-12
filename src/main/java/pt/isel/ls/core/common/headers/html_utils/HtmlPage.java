package pt.isel.ls.core.common.headers.html_utils;

import pt.isel.ls.core.common.headers.Html;
import pt.isel.ls.core.utils.writable.Writable;

public class HtmlPage extends Html {

    public HtmlPage(String title, Writable... c) {
        super(
                new HtmlElem("html",
                        new HtmlElem("head", new HtmlElem("Title", new HtmlText(title))),
                        new HtmlElem("body", c)
                )
        );
    }
}
