package pt.isel.ls.view.command;

import java.io.*;

public class OptionsView extends CommandView {
    private final static String FILE_NAME = "command_options";
    private File file = new File(getClass().getClassLoader().getResource(FILE_NAME).getFile());

    @Override
    public void printAllInfo() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while(line != null) {
                System.out.println(line);
                line = in.readLine();
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("WARNING: ["+FILE_NAME+"] not found.");
        }
    }
}
