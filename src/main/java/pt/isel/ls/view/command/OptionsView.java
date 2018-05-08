package pt.isel.ls.view.command;

import java.io.*;

public class OptionsView extends CommandView {
    private final static String FILE_NAME = "command_options";
    private InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);

    @Override
    protected void allInfo() {
        StringBuilder strBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
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
