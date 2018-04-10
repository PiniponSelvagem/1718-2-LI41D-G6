package pt.isel.ls.core.headers;

public class Plain extends Header {
    private static final String tablePadding = "%20s",
                                tableHLine   = "--------------------";

    @Override
    public void addTitle(String title) {
        text.append(title).append(System.lineSeparator());
    }

    @Override
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

    @Override
    public void addObject(String id, String[] fieldName, String[] value) {
        addTitle(id+":");
        for (int i=0; i<fieldName.length; ++i) {
            text.append(" > ").append(fieldName[i]).append(": ")
                    .append(value[i])
                    .append(System.lineSeparator());
        }
    }

    @Override
    public void addEmptyLine() {
        text.append(System.lineSeparator());
    }
}
