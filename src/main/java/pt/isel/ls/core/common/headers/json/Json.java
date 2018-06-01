package pt.isel.ls.core.common.headers.json;

import pt.isel.ls.core.common.headers.Header;

public class Json implements Header {

    private StringBuilder text = new StringBuilder();

    @Override
    public String getString() {
        return text.toString();
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
