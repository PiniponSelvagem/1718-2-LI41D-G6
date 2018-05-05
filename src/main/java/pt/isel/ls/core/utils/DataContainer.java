package pt.isel.ls.core.utils;

import pt.isel.ls.core.common.headers.Header;

import java.util.LinkedList;

public class DataContainer {
    private Header header;
    private LinkedList<Object> data = new LinkedList<>();

    public DataContainer(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }


    public void add(Object obj) {
        data.add(obj);
    }

    public Object getData(int i) {
        return data.get(i);
    }

    public int size() {
        return data.size();
    }
}
