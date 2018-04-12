package pt.isel.ls.view.command;

import java.io.*;

public class OptionsView extends CommandView {
    private ClassLoader classLoader = getClass().getClassLoader();
    private File file = new File(classLoader.getResource("command_options").getFile());

    @Override
    public void printAllInfo() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while(line != null) {
                System.out.println(line);
                line = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
