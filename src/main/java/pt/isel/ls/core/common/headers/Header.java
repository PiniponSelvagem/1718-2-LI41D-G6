package pt.isel.ls.core.common.headers;

import pt.isel.ls.core.common.CommonCmd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static pt.isel.ls.core.strings.CommandEnum.DIR_SEPARATOR;

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
     * @return Returns the path+methodname without the 1st "/",
     * while adding it between path and methodname.
     * example: text/html
     */
    public String getPathAndMethodName() {
        return getPath().substring(1)+DIR_SEPARATOR.toString()+getMethodName();
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
