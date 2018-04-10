package pt.isel.ls.core.headers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Header {
    protected StringBuilder text = new StringBuilder();
    public String fileName;

    Header() {
        open();
    }

    /**
     * @return Returns buildedString.
     */
    public StringBuilder getBuildedString() {
        return text;
    }

    /**
     * Prepare view
     */
    protected void open() {
        ;
    }

    /**
     * Add title to to view.
     * if supported, this text should appear bigger than the normal text.
     * @param title
     */
    public abstract void addTitle(String title);

    /**
     * Add table to the view
     * @param columns Names for each column
     * @param data String[y][x] --> y=lines, x=columns
     */
    public abstract void addTable(String[] columns, String[][] data);

    /**
     * Represent object
     * @param id Name to identify the object
     * @param fieldName Names of the multiple fields
     * @param value Values to place in each field
     */
    public abstract void addObject(String id, String[] fieldName, String[] value);

    /**
     * If supported, adds a new line to the view.
     */
    public void addEmptyLine() {
        ;
    }

    /**
     * Close view
     */
    public void close() {
        ;
    }

    /**
     * Write builded text to file.
     */
    public void writeToFile() {
        if (fileName != null) {
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(text.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
