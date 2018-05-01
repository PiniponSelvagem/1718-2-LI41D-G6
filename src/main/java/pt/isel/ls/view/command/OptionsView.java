package pt.isel.ls.view.command;

import java.io.*;

public class OptionsView extends CommandView {
    private final static String FILE_NAME = "command_options";
    private File file = new File(getClass().getClassLoader().getResource(FILE_NAME).getFile());

    @Override
    protected void allInfo() {
        StringBuilder strBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while(line != null) {
                strBuilder.append(line).append(System.lineSeparator());
                line = in.readLine();
            }
        } catch (NullPointerException | IOException e) {
            infoString = "WARNING: ["+FILE_NAME+"] not found.";
        }
        infoString = strBuilder.toString();
    }
}
