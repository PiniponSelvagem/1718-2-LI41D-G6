package pt.isel.ls.core.common.headers;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class Json extends Header {

    @Override
    public String getMethodName() {
        return JSON.toString();
    }

    @Override
    public String getPath() {
        return ""+DIR_SEPARATOR+APPLICATION;
    }


    @Override
    protected void open() {
        text.append('{');
    }

    @Override
    public void addTitle(String title) {
        text.append("\"title\": \"").append(title).append("\",");
    }

    @Override
    public void addTable(String title, String[] columns, String[][] data) {
        text.append("\"").append(title).append("\": {");

        for(int i=0; i<data.length; ++i) {
            addObject("Entry"+i, columns, data[i]);
            text.append(',');
        }
        text.deleteCharAt(text.length()-1); //delete last ','
        text.append('}');
    }

    @Override
    public void addObject(String nameId, String[] fieldName, String[] value) {
        text.append("\"").append(nameId).append("\": {");

        for(int y=0; y<fieldName.length && y<value.length; ++y) {
            text.append("\"").append(fieldName[y]).append("\": \"").append(value[y]).append("\",");
        }
        text.deleteCharAt(text.length()-1); //delete last ','
        text.append("}");
    }

    @Override
    public void close() {
        text.append('}');
    }
}
