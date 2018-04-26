package pt.isel.ls.phase3_code.html;

import pt.isel.ls.phase3_code.common.Writable;

public class HtmlPage extends Html{
    
    public HtmlPage(String title, Writable... c) {
        super(
                new HtmlElem("html",
                        new HtmlElem("head", new HtmlElem("Title", new HtmlText(title))),
                        new HtmlElem("body", c)
                )
        );
    }
}
