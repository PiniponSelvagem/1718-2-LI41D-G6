package pt.isel.ls.phase3_code.html;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringEscapeUtils;

import pt.isel.ls.phase3_code.common.Writable;

public class HtmlText implements Writable {

    public final String _text;
    
    public HtmlText(String text) {
        _text = text;
    }

    @Override
    public void writeTo(Writer w) throws IOException {
        w.write(StringEscapeUtils.escapeHtml(_text));        
    }
}
