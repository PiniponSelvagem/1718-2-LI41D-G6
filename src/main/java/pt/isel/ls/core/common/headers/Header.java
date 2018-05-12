package pt.isel.ls.core.common.headers;

import pt.isel.ls.core.common.CommonCmd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Header extends CommonCmd {
    protected StringBuilder text = new StringBuilder();
    public String fileName;
    private final static String dirFiles = "header_files";

    /**
     * @return Returns buildedString.
     */
    public String getBuildedString() {
        return text.toString();
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
