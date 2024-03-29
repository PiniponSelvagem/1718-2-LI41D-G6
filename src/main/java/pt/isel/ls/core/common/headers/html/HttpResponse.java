package pt.isel.ls.core.common.headers.html;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private final HttpStatusCode _status;
    private final String _body;
    private final Charset _charset = Charset.forName("UTF-8");
    private final Map<String, String> _headers = new HashMap<>();

    public HttpResponse(HttpStatusCode status) {
        this(status, null);
    }

    public HttpResponse(HttpStatusCode status, String body) {
        _status = status;
        _body = body;
    }

    public HttpResponse withHeader(String name, String value) {
        _headers.put(name, value);
        return this;
    }

    public void send(HttpServletResponse resp) throws IOException {
        for (Map.Entry<String, String> entry : _headers.entrySet()) {
            resp.addHeader(entry.getKey(), entry.getValue());
        }
        if (_body == null) {
            sendWithoutBody(resp);
        } else {
            sendWithBufferedBody(resp);
        }
    }

    private void sendWithoutBody(HttpServletResponse resp) throws IOException {
        resp.setStatus(_status.valueOf());
    }

    private void sendWithBufferedBody(HttpServletResponse resp) throws IOException {

        ByteArrayOutputStream _os = new ByteArrayOutputStream();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_os, _charset))) {
            writer.write(_body);
        }
        byte[] bytes = _os.toByteArray();
        resp.setStatus(_status.valueOf());
        String ctype = String.format("%s;charset=%s", "text/html", _charset.name());
        resp.setContentType(ctype);
        resp.setContentLength(bytes.length);
        OutputStream ros = resp.getOutputStream();
        ros.write(bytes);
        ros.close();
    }
}
