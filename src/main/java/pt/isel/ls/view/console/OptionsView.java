package pt.isel.ls.view.console;

import pt.isel.ls.core.utils.DataContainer;
import pt.isel.ls.view.CommandView;

import java.io.*;

public class OptionsView extends CommandView {
    private final static String FILE_NAME = "command_options";
    private InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);

    public OptionsView(DataContainer data) {
        super(data);
    }

    @Override
    public String getString() {
        StringBuilder strBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = in.readLine();
            while(line != null) {
                strBuilder.append(line).append(System.lineSeparator());
                line = in.readLine();
            }
        } catch (NullPointerException | IOException e) {
            return "WARNING: ["+FILE_NAME+"] not found.";
        }
        return strBuilder.toString();
    }

    @Override
    public final void createView() {
        ;
    }
}
