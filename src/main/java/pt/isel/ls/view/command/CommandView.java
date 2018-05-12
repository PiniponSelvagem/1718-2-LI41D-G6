package pt.isel.ls.view.command;

import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.DataContainer;

public abstract class CommandView {
    protected String infoString;
    protected DataContainer data;
    protected Header header;

    /**
     * infoString -> information ready to be, for example printed to console.
     * NOTE: Overriding this method will ignore Headers for the view that is overriding.
     *       For that, place the string at: {@link CommandView#infoString}
     *       Check {@link ExitView} as an example.
     */
    protected void allInfo() {
        header = data.getHeader();

        if (header != null) {
            if (header instanceof Plain)     infoString = toPlain((Plain) header);
            else if (header instanceof Html) infoString = toHtml((Html) header);
            else if (header instanceof Json) infoString = toJson((Json) header);

            header.writeToFile();
        }
    }

    /**
     * @return String, information ready to be, for example printed to console.
     */
    public final String getAllInfoString() {
        if (infoString == null) {
            allInfo();
        }
        return infoString;
    }

    /**
     * @return Returns String ready to use for Plain format
     */
    protected String toPlain(Plain header) {
        return "INFO: Plain format for requested command not implemented.";
    }

    /**
     * @return Returns String ready to use for HTML format
     */
    protected String toHtml(Html header) {
        return "<html><body><h1>INFO: HTML format for requested command not implemented.</h1></body></html>";
    }

    /**
     * @return Returns String ready to use for JSON format
     */
    protected String toJson(Json header) {
        return "{\"INFO\":\"JSON format for requested command not implemented.\"}";
    }

    /**
     * Example usage:
     * CommandView cmdView = cmd.execute(...);
     * DataContainer data = cmdView.getData();
     * Cinema c = (Cinema) data.getData(0);    //value '0' is the first one, going to a max of 'data.size()'
     * System.out.println(c.getName());
     *
     * @return Returns DataContainer with all the information for this type of view.
     */
    public final DataContainer getData() {
        return data;
    }
}
