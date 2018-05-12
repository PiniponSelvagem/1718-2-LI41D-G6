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

    public void addArray(String[] columns, String[][] data) {
        text.append("[");
        for(int i=0; i<data.length; ++i) {
            addObject(columns, data[i]);
            text.append(',');
        }
        if (data.length > 0)
            text.deleteCharAt(text.length()-1); //delete last ','
        text.append(']');
    }

    public void addObject(String[] fieldName, String[] value) {
        if (fieldName==null || value==null) {
            text.append("{}");
            return;
        }

        text.append("{");

        for(int y=0; y<fieldName.length && y<value.length; ++y) {
            text.append("\"").append(fieldName[y]).append("\": \"").append(value[y]).append("\",");
        }
        text.deleteCharAt(text.length()-1); //delete last ','
        text.append("}");
    }
}
