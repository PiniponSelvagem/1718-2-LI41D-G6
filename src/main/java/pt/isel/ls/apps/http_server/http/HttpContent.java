package pt.isel.ls.apps.http_server.http;

import pt.isel.ls.phase3_code.common.Writable;

public interface HttpContent extends Writable {
    String getMediaType();    
}
