package pt.isel.ls.core.common.headers.html_utils;

import pt.isel.ls.core.utils.writable.Writable;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class HtmlElem implements Writable {

    private final String _name;
    private boolean end = true;
        
    public HtmlElem(String name, Writable...cs) {
        _name = name;
        _content.addAll(Arrays.asList(cs));
    }

    public HtmlElem(Writable...cs) {
        _name = null;
        _content.addAll(Arrays.asList(cs));
    }

    public HtmlElem(String name, boolean end, Writable...cs) {
        _name = name;
        this.end = end;
        _content.addAll(Arrays.asList(cs));
    }
    
    private final Map<String,String> _attrs = new HashMap<String,String>();

    public final HtmlElem withAttr(String name, String value) {
        _attrs.put(name, value);
        return this;
    }

    public static HtmlElem submit(String text) {
        return new HtmlElem("input")
                .withAttr("type", "submit")
                .withAttr("value", text);
    }
    public static HtmlElem submit(String name, String text) {
        return new HtmlElem("input")
                .withAttr("class", name)
                .withAttr("type", "submit")
                .withAttr("value", text);
    }
    
    private final List<Writable> _content = new ArrayList<Writable>();
    public final HtmlElem withContent(Writable... w) {
        _content.addAll(Arrays.asList(w));
        return this;
    }
    
    @Override
    public void writeTo(Writer w) throws IOException {
        if (_name != null) {
            w.write(String.format("<%s",_name));
            for(Map.Entry<String,String> entry : _attrs.entrySet()) {
                w.write(String.format(" %s='%s'",entry.getKey(), entry.getValue()));
            }
            w.write(">");
        }
        for(Writable c : _content) {
            c.writeTo(w);
        }
        if (_name != null && end) {
            w.write(String.format("</%s>", _name));
        }
    }
}
