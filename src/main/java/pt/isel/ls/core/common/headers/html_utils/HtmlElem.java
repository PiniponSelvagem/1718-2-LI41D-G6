package pt.isel.ls.core.common.headers.html_utils;

import pt.isel.ls.core.utils.writable.Writable;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class HtmlElem implements Writable {

    private final String _name;
        
    public HtmlElem(String name, Writable...cs) {
        _name = name;
        _content.addAll(Arrays.asList(cs));
    }
    
    private final Map<String,String> _attrs = new HashMap<String,String>();
    public final HtmlElem withAttr(String name, String value) {
        _attrs.put(name, value);
        return this;
    }
    
    private final List<Writable> _content = new ArrayList<Writable>();
    public final HtmlElem withContent(Writable... w) {
        _content.addAll(Arrays.asList(w));
        return this;
    }
    
    @Override
    public void writeTo(Writer w) throws IOException {
        w.write(String.format("<%s",_name));
        for(Map.Entry<String,String> entry : _attrs.entrySet()) {
            w.write(String.format(" %s='%s'",entry.getKey(), entry.getValue()));
        }
        w.write(">");
        for(Writable c : _content) {
            c.writeTo(w);
        }
        w.write(String.format("</%s>",_name));
    }
}
