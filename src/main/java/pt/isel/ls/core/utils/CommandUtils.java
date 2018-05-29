package pt.isel.ls.core.utils;

import pt.isel.ls.core.common.commands.*;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.common.headers.html_utils.HtmlPage;
import pt.isel.ls.core.utils.directorytree.DirectoryNode;
import pt.isel.ls.core.utils.directorytree.DirectoryTree;
import pt.isel.ls.view.*;

import java.util.HashMap;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class CommandUtils {

    private String root = ROOT_DIR.toString();
    private DirectoryTree cmdTree = new DirectoryTree(new DirectoryNode(root));
    private DirectoryTree headersTree = new DirectoryTree(new DirectoryNode(root));
    private HashMap<String, String> cmdViewMap = new HashMap<>();
    private HashMap<String, String> dirID = new HashMap<>();
    private HashMap<String, Boolean> paramsCheck = new HashMap<>();

    /**
     * Start core utils.
     */
    public CommandUtils() {
        initializeCommandsTree();
        initializeDirID();
        initializeParamsCheck();
        initializeHeadersTree();
        initializeCommandViewMap();
    }


    /**
     * Fills the cmdTree with all the possible commands available.
     */
    private void initializeCommandsTree() {
        /* Internal commands */
        cmdTree.add(new Options());                 //"OPTIONS"
        cmdTree.add(new Exit());                    //"EXIT"
        cmdTree.add(new Listen());                  //"LISTEN"


        /* Commands related to MOVIES */
        cmdTree.add(new PostMovies());              //"POST /movies"
        cmdTree.add(new GetMovies());               //"GET /movies"
        cmdTree.add(new GetMovieID());              //"GET /movies/{mid}"


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
    private void initializeDirID() {
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
    private void initializeParamsCheck() {
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
     * Fills the headersTree with all the possible headers available.
     */
    private void initializeHeadersTree() {
        /* Directory: text */
        headersTree.add(new Plain());    //"PLAIN"
        headersTree.add(new HtmlPage()); //"HTML"

        /* Directory: application */
        headersTree.add(new Json());     //"JSON"
    }

    /**
     * Fills the cmdViewMap, linking each command to their view.
     */
    private void initializeCommandViewMap() {
        /* Internal commands */
        cmdViewMap.put(Options.class.getSimpleName(), OptionsView.class.getName()); //"OPTIONS"
        cmdViewMap.put(Exit.class.getSimpleName(),    ExitView.class.getName());    //"EXIT"


        /* Commands related to MOVIES */
        cmdViewMap.put(PostMovies.class.getSimpleName(), PostMoviesView.class.getName());       //"POST /movies"
        cmdViewMap.put(GetMovies.class.getSimpleName(),  GetMoviesView.class.getName());  //"GET /movies"
        cmdViewMap.put(GetMovieID.class.getSimpleName(), GetMovieIDView.class.getName()); //"GET /movies/{mid}"


        /* Commands related to CINEMAS */
        cmdViewMap.put(PostCinemas.class.getSimpleName(), PostCinemasView.class.getName()); //"POST /cinemas"
        cmdViewMap.put(GetCinemas.class.getSimpleName(),  GetCinemasView.class.getName());  //"GET /cinemas"
        cmdViewMap.put(GetCinemaID.class.getSimpleName(), GetCinemaIDView.class.getName()); //"GET /cinemas/{cid}"


        /* Commands related to CINEMAS->THEATERS */
        cmdViewMap.put(PostCinemaIDTheaters.class.getSimpleName(), PostCinemaIDTheatersView.class.getName()); //"POST /cinemas/{cid}/theaters"
        cmdViewMap.put(GetCinemaIDTheaters.class.getSimpleName(),  GetCinemaIDTheatersView.class.getName());  //"GET /cinemas/{cid}/theaters"
        cmdViewMap.put(GetCinemaIDTheaterID.class.getSimpleName(), GetCinemaIDTheaterIDView.class.getName()); //"GET /cinemas/{cid}/theaters/{tid}"


        /* Commands related to CINEMAS->THEATERS->SESSIONS */
        cmdViewMap.put(PostCinemaIDTheaterIDSessions.class.getSimpleName(),     PostCinemaIDTheaterIDSessionsView.class.getName());//"POST /cinemas/{cid}/theaters/{tid}/sessions"
        cmdViewMap.put(GetCinemaIDTheaterIDSessions.class.getSimpleName(),      GetCinemaIDTheaterIDSessionsView.class.getName()); //"GET /cinemas/{cid}/theaters/{tid}/sessions"
        cmdViewMap.put(GetCinemaIDTheaterIDSessionsToday.class.getSimpleName(), GetCinemaIDTheaterIDSessionsView.class.getName()); //"GET /cinemas/{cid}/theaters/{tid}/sessions/today"


        /* Commands related to CINEMAS->SESSIONS */
        cmdViewMap.put(GetCinemaIDSessions.class.getSimpleName(),       GetCinemaIDSessionsView.class.getName());       //"GET /cinemas/{cid}/sessions"
        cmdViewMap.put(GetCinemaIDSessionID.class.getSimpleName(),      GetCinemaIDSessionIDView.class.getName());      //"GET /cinemas/{cid}/sessions/{sid}"
        cmdViewMap.put(GetCinemaIDSessionsToday.class.getSimpleName(),  GetCinemaIDSessionsDateIDView.class.getName()); //"GET /cinemas/{cid}/sessions/today"
        cmdViewMap.put(GetCinemaIDSessionsDateID.class.getSimpleName(), GetCinemaIDSessionsDateIDView.class.getName()); //"GET /cinemas/{cid}/sessions/date/{dmy}"
        cmdViewMap.put(GetMovieIDSessionsDateID.class.getSimpleName(),  GetMovieIDSessionsDateIDView.class.getName());  //"GET /movies/{mid}/sessions/date/{dmy}"


        /* Commands related to Tickets */
        cmdViewMap.put(PostCinemaIDTheaterIDSessionIDTickets.class.getSimpleName(),         PostCinemaIDTheaterIDSessionIDTicketsView.class.getName());         //"POST /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdViewMap.put(GetCinemaIDTheaterIDSessionIDTickets.class.getSimpleName(),          GetCinemaIDTheaterIDSessionIDTicketsView.class.getName());          //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdViewMap.put(GetCinemaIDTheaterIDSessionIDTicketID.class.getSimpleName(),         GetCinemaIDTheaterIDSessionIDTicketIDView.class.getName());         //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid}"
        cmdViewMap.put(GetCinemaIDTheaterIDSessionIDTicketsAvailable.class.getSimpleName(), GetCinemaIDTheaterIDSessionIDTicketsAvailableView.class.getName()); //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available"
        cmdViewMap.put(DeleteCinemaIDTheaterIDSessionIDTicket.class.getSimpleName(),        DeleteView.class.getName());                                        //"DELETE /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
    }

    /**
     * Used by {@link #initializeParamsCheck()}
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
     * @return Returns the command Tree
     */
    public DirectoryTree getHeadersTree() {
        return headersTree;
    }

    /**
     * @return Returns cmdView hashmap <String: Command, String: CommandView>
     */
    public HashMap<String, String> getCmdViewMap() {
        return cmdViewMap;
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
