package pt.isel.ls.core.common.headers.html;

import pt.isel.ls.core.common.headers.Header;
import pt.isel.ls.core.utils.writable.Writable;

public abstract class Html implements Header {

    protected Writable _content;

    public static Writable style(Writable c) { return new HtmlElem("style", c);}
    public static Writable text(String s) { return new HtmlText(s);}
    public static Writable h1(Writable... c) { return new HtmlElem("h1",c);}
    public static Writable h2(Writable... c) { return new HtmlElem("h2",c);}
    public static Writable h3(Writable... c) { return new HtmlElem("h3",c);}
    public static Writable form(String method, String url, Writable... c) {
        return new HtmlElem("form",c)
            .withAttr("method", method)
            .withAttr("action", url);
    }
    public static Writable form(String method, String url, String id, Writable... c) {
        return new HtmlElem("form",c)
                .withAttr("method", method)
                .withAttr("action", url)
                .withAttr("id", id);
    }
    public static Writable label(String to, String text) {
        return new HtmlElem("label", new HtmlText(text))
            .withAttr("for", to);
    }
    public static Writable textInput(String name) {
        return new HtmlElem("input")
            .withAttr("type", "text")
            .withAttr("name", name);
    }
    public static Writable hiddenInput(String name, String value) {
        return new HtmlElem("input")
                .withAttr("type", "hidden")
                .withAttr("name", name)
                .withAttr("value", value);
    }
    public static Writable ul(Writable... c) {
        return new HtmlElem("ul",c);
    }
    public static Writable li(Writable...c) {
        return new HtmlElem("li",c);
    }
    public static Writable a(String href, String t) {
        return new HtmlElem("a", text(t))
            .withAttr("href", href);
    }
    public static Writable breakLine() {
        return new HtmlElem("br", false);
    }
    public static Writable table(Writable[] c) {
        return new HtmlElem("table", c)
                .withAttr("border", "1");
    }
    public static Writable tableWithName(String name, Writable[] c) {
        return new HtmlElem("table", c)
                .withAttr("class", name)
                .withAttr("border", "1");
    }

    public static Writable tr(Writable[] c) {
        return new HtmlElem("tr").withContent(c);
    }
    public static Writable th(Writable c) {
        return new HtmlElem("th").withContent(c);
    }
    public static Writable td(Writable c) {
        return new HtmlElem("td")
                .withAttr("align", "center")
                .withContent(c);
    }
    public static Writable tdCustom(Writable c, String color) {
        return new HtmlElem("td")
                .withAttr("valign", "middle")
                .withAttr("height", "38px")
                .withAttr("width", "38px")
                .withAttr("align", "center")
                .withAttr("bgcolor", color)
                .withContent(c);
    }
    public static Writable select(Writable[] c, String name, String formName) {
        return new HtmlElem("select")
                .withAttr("name", name)
                .withAttr("size", "1")
                .withAttr("form", formName)
                .withContent(c);
    }
    public static Writable option(String value, Writable c) {
        return new HtmlElem("option")
                .withAttr("value", value)
                .withContent(c);
    }
    public static Writable multipleElems(Writable[] c) {
        return new HtmlElem().withContent(c);
    }
}
