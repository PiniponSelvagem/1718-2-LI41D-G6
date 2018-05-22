package pt.isel.ls.apps.http_server.http.htmlserverpages;
import java.util.HashMap;

public class PagesUtils {

    private HashMap<String, ServerPage> pagesMap = new HashMap<>();

    /**
     * Start PagesUtils utils.
     */
    public PagesUtils() {
        initializePagesTree();
    }


    /**
     * Fills the pagesTree with all the custom pages that arent accessible from normal Commands
     */
    private void initializePagesTree() {
        pagesMap.put("/", new Index());     //index page
    }

    /**
     * @return Returns the page requested
     */
    public ServerPage getPage(String path) {
        return pagesMap.get(path);
    }
}
