package pt.isel.ls.core.common.headers;

import pt.isel.ls.core.common.headers.html_utils.HtmlElem;
import pt.isel.ls.core.common.headers.html_utils.HtmlText;
import pt.isel.ls.core.utils.writable.CompositeWritable;
import pt.isel.ls.core.utils.writable.Writable;

import java.io.*;
import java.nio.charset.Charset;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;
import static pt.isel.ls.core.strings.CommandEnum.HTML;
import static pt.isel.ls.core.strings.CommandEnum.TEXT;

public class Html extends Header {

    private Writable _content;
    
    public Html(Writable... cs) {
        _content = new CompositeWritable(cs);
    }

    @Override
    public String getMethodName() {
        return HTML.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+TEXT;
    }
    
    public static Writable text(String s) { return new HtmlText(s);}
    public static Writable h1(Writable... c) { return new HtmlElem("h1",c);}
    public static Writable h2(Writable... c) { return new HtmlElem("h2",c);}
    public static Writable h3(Writable... c) { return new HtmlElem("h3",c);}
    public static Writable form(String method, String url, Writable... c) {
        return new HtmlElem("form",c)
            .withAttr("method", method)
            .withAttr("action", url);
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
    public static Writable table(Writable[] c) {
        return new HtmlElem("table", c)
                .withAttr("border", "1");
    }
    public static Writable tr(Writable[] c) {
        return new HtmlElem("tr").withContent(c);
    }
    public static Writable th(Writable c) {
        return new HtmlElem("th").withContent(c);
    }
    public static Writable td(Writable c) {
        return new HtmlElem("td").withContent(c);
    }

    @Override
    public String getBuildedString() {
        ByteArrayOutputStream _os = new ByteArrayOutputStream();
        Charset _charset = Charset.forName("UTF-8");
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_os, _charset))){
            _content.writeTo(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _os.toString();
    }
}
