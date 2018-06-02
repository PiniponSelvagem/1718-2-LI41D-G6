package pt.isel.ls.core.utils;

import pt.isel.ls.core.common.commands.*;
import pt.isel.ls.core.utils.directorytree.DirectoryNode;
import pt.isel.ls.core.utils.directorytree.DirectoryTree;
import pt.isel.ls.core.utils.view_map.*;
import pt.isel.ls.view.console.ExitView;
import pt.isel.ls.view.console.OptionsView;

import java.util.HashMap;

import static pt.isel.ls.core.common.headers.HeadersAvailable.*;
import static pt.isel.ls.core.strings.CommandEnum.*;

public class CommandUtils {

    private String root = ROOT_DIR.toString();
    private DirectoryTree cmdTree = new DirectoryTree(new DirectoryNode(root));
    private HashMap<String, String> dirID = new HashMap<>();
    private HashMap<String, Boolean> paramsCheck = new HashMap<>();

    public final String defaultHeaderType;
    private static HashMap<String, HashMap<String, String>> viewTypeMap = new HashMap<>();

    /**
     * Start core utils.
     */
    public CommandUtils(String defaultHeaderType) {
        this.defaultHeaderType = defaultHeaderType;
        initCommandsTree();
        initDirID();
        initParamsCheck();
        initViewMaps();
        addConsoleViewsToDefaultHeaderTypeMap(defaultHeaderType);
    }


    /**
     * Fills the cmdTree with all the possible commands available.
     */
    private void initCommandsTree() {
        /* Internal commands */
        cmdTree.add(new Options());                 //"OPTIONS"
        cmdTree.add(new Exit());                    //"EXIT"
        cmdTree.add(new Listen());                  //"LISTEN"


        /* Commands related to MOVIES */
        cmdTree.add(new PostMovies());              //"POST /movies"
        cmdTree.add(new GetMovies());               //"GET /movies"
        cmdTree.add(new GetMovieID());              //"GET /movies/{mid}"
        cmdTree.add(new GetMoviesTMDB());          //"GET /movies/tmdb"


        /* Commands related to CINEMAS */
        cmdTree.add(new PostCinemas());             //"POST /cinemas"
        cmdTree.add(new GetCinemas());              //"GET /cinemas"
        cmdTree.add(new GetCinemaID());             //"GET /cinemas/{cid}"


        /* Commands related to CINEMAS->THEATERS */
        cmdTree.add(new PostCinemaIDTheaters());    //"POST /cinemas/{cid}/theaters"
        cmdTree.add(new GetCinemaIDTheaters());     //"GET /cinemas/{cid}/theaters"
        cmdTree.add(new GetCinemaIDTheaterID());    //"GET /cinemas/{cid}/theaters/{tid}"


        /* Commands related to CINEMAS->THEATERS->SESSIONS */
        cmdTree.add(new PostCinemaIDTheaterIDSessions());       //"POST /cinemas/{cid}/theaters/{tid}/sessions"
        cmdTree.add(new GetCinemaIDTheaterIDSessions());        //"GET /cinemas/{cid}/theaters/{tid}/sessions"
        cmdTree.add(new GetCinemaIDTheaterIDSessionsToday());   //"GET /cinemas/{cid}/theaters/{tid}/sessions/today"


        /* Commands related to CINEMAS->SESSIONS */
        cmdTree.add(new GetCinemaIDSessions());        //"GET /cinemas/{cid}/sessions"
        cmdTree.add(new GetCinemaIDSessionID());       //"GET /cinemas/{cid}/sessions/{sid}"
        cmdTree.add(new GetCinemaIDSessionsToday());   //"GET /cinemas/{cid}/sessions/today"
        cmdTree.add(new GetCinemaIDSessionsDateID());  //"GET /cinemas/{cid}/sessions/date/{dmy}"
        cmdTree.add(new GetMovieIDSessionsDateID());   //"GET /movies/{mid}/sessions/date/{dmy}"


        /* Commands related to Tickets */
        cmdTree.add(new PostCinemaIDTheaterIDSessionIDTickets());           //"POST /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdTree.add(new GetCinemaIDTheaterIDSessionIDTickets());            //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdTree.add(new GetCinemaIDTheaterIDSessionIDTicketID());           //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid}"
        cmdTree.add(new GetCinemaIDTheaterIDSessionIDTicketsAvailable());   //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available"
        cmdTree.add(new DeleteCinemaIDTheaterIDSessionIDTicket());          //"DELETE /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
    }

    /**
     * Fills the HashMap of Directory->ID.
     * This HashMap is used to know which current directory its at when we find a INT value,
     * there we replace that value with the value thats in this HashMap, so the commandTree can
     * continue its way in the directories.
     */
    private void initDirID() {
        dirID.put(CINEMAS.toString(),  CINEMA_ID_FULL.toString());
        dirID.put(MOVIES.toString(),   MOVIE_ID_FULL.toString());
        dirID.put(THEATERS.toString(), THEATER_ID_FULL.toString());
        dirID.put(SESSIONS.toString(), SESSION_ID_FULL.toString());
        dirID.put(DATE.toString(),     DATE_ID_FULL.toString());
        dirID.put(TICKETS.toString(),  TICKET_ID_FULL.toString());
    }

    /**
     * Fills the HashMap with the all available parameters, with value TRUE.
     * This way, when we can check if its a valid parameter with {@link #validParam(String param)}.
     */
    private void initParamsCheck() {
        putParamsCheck(CINEMA_ID.toString());
        putParamsCheck(NAME.toString());
        putParamsCheck(CITY.toString());

        putParamsCheck(MOVIE_ID.toString());
        putParamsCheck(TITLE.toString());
        putParamsCheck(YEAR.toString());
        putParamsCheck(DURATION.toString());

        putParamsCheck(THEATER_ID.toString());
        putParamsCheck(ROWS.toString());
        putParamsCheck(SEATS_ROW.toString());

        putParamsCheck(SESSION_ID.toString());
        putParamsCheck(DATE_PARAM.toString());

        putParamsCheck(TICKET_ID.toString());
        putParamsCheck(AVAILABLE.toString());

        putParamsCheck(SERVER_PORT.toString());
    }

    /**
     * Fills the multiple view maps, linking each command to their view based on header type.
     */
    private void initViewMaps() {
        viewTypeMap.put(TEXT_HTML.toString(),  new ViewMapHtml().getViewMap());
        viewTypeMap.put(TEXT_PLAIN.toString(), new ViewMapPlain().getViewMap());
        viewTypeMap.put(APP_JSON.toString(),   new ViewMapJson().getViewMap());
    }

    /**
     * Fills the Map that is assigned as default header view, with the views of the console commands.
     * @param defaultHeaderType Default header, when header is omitted in the command input,
     *                          the requested view will be from this type of header.
     */
    private void addConsoleViewsToDefaultHeaderTypeMap(String defaultHeaderType) {
        HashMap<String, String> map = viewTypeMap.get(defaultHeaderType);

        /* Internal commands */
        map.put(Options.class.getSimpleName(), OptionsView.class.getName()); //"OPTIONS"
        map.put(Exit.class.getSimpleName(),    ExitView.class.getName());    //"EXIT"
    }


    /**
     * Used by {@link #initParamsCheck()}
     * @param param Add this KEY to the HashMap with value TRUE
     */
    private void putParamsCheck(String param) {
        paramsCheck.put(param, true);
    }

    /**
     * @return Returns the command Tree
     */
    public DirectoryTree getCmdTree() {
        return cmdTree;
    }

    /**
     * @return Returns cmdView hashmap <String: Command, String: CommandView>
     */
    public HashMap<String, String> getCmdViewMap(String headerType) {
        return viewTypeMap.get(headerType);
    }

    /**
     * @return Returns the root directory name
     */
    public String getRootName() {
        return root;
    }

    /**
     * Returns the directory ID, useful specially to replace the ID value found in user core path
     * so it can be replaced with a useful name to keep searching for the core in  the core Tree.
     * @param dir Directory that you want to get the corresponding string ID.
     * @return Returns corresponding directory ID.
     */
    public String getDirID(String dir) {
        return dirID.get(dir);
    }

    /**
     * Returns TRUE if its a valid parameter.
     * @param param Check if this is valid parameter.
     * @return Returns TRUE if yes, FALSE if is invalid aka not found.
     */
    public boolean validParam(String param) {
        if (paramsCheck.get(param) == null) return false;
        else return paramsCheck.get(param);
    }
}
