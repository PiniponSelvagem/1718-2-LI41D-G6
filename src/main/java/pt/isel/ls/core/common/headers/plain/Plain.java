package pt.isel.ls.core.common.headers.plain;

import pt.isel.ls.core.common.headers.Header;

public class Plain implements Header {
    private static final String tablePadding = "%20s",
                                tableHLine   = "--------------------";

    private StringBuilder text = new StringBuilder();

    @Override
    public String getString() {
        return text.toString();
    }

    public void addTitle(String title) {
        text.append(title).append(System.lineSeparator());
    }

    public void addTable(String[] columns, String[][] data) {
        for (String column : columns) {
            text.append(String.format(tablePadding, column));
        }
        addEmptyLine();
        for (String column : columns) {
            text.append(tableHLine);
        }
        addEmptyLine();
        for (int y=0; y<data.length; ++y) {
            for (int x=0; x<data[y].length; ++x) {
                text.append(String.format(tablePadding, data[y][x]));
            }
            addEmptyLine();
        }
    }

    public void addDetailed(String nameId, String[] fieldName, String[] value) {
        addTitle(nameId+":");
        for (int i=0; i<fieldName.length; ++i) {
            text.append(" > ").append(fieldName[i]).append(": ")
                    .append(value[i])
                    .append(System.lineSeparator());
        }
    }

    private void addEmptyLine() {
        text.append(System.lineSeparator());
    }
}
