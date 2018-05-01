package pt.isel.ls.core.headers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Header {
    protected StringBuilder text = new StringBuilder();
    public String fileName;
    private final static String dirFiles = "header_files";

    Header() {
        open();
    }

    /**
     * @return Returns buildedString.
     */
    public String getBuildedString() {
        return text.toString();
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
    public abstract void addTable(String title, String[] columns, String[][] data);

    /**
     * Represent object
     * @param nameId Name to identify the object
     * @param fieldName Names of the multiple fields
     * @param value Values to place in each field
     */
    public abstract void addObject(String nameId, String[] fieldName, String[] value);

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
                File file = new File(dirFiles);
                file.mkdir();   //create dirFiles directory if dosent exits
                writer = new BufferedWriter(new FileWriter(file.getPath()+"/"+fileName));
                writer.write(text.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
