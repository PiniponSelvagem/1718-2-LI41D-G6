package pt.isel.ls.core.utils;

import pt.isel.ls.core.common.commands.*;
import pt.isel.ls.core.common.headers.*;
import pt.isel.ls.core.utils.directorytree.DirectoryNode;
import pt.isel.ls.core.utils.directorytree.DirectoryTree;

import java.util.HashMap;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class CommandUtils {

    private String root = ROOT_DIR.toString();
    private DirectoryTree cmdTree = new DirectoryTree(new DirectoryNode(root));
    private DirectoryTree headersTree = new DirectoryTree(new DirectoryNode(root));
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
    }


    /**
     * Fills the cmdTree with all the possible commands available.
     */
    private void initializeCommandsTree() {
        /* Internal commands */
        cmdTree.add(new CommandBuilder(new Options()));                 //"OPTIONS"
        cmdTree.add(new CommandBuilder(new Exit()));                    //"EXIT"
        cmdTree.add(new CommandBuilder(new Listen()));                  //"LISTEN"
        cmdTree.add(new CommandBuilder(new Test()));                    //"TEST"

        /* Commands related to MOVIES */
        cmdTree.add(new CommandBuilder(new PostMovies()));              //"POST /movies"
        cmdTree.add(new CommandBuilder(new GetMovies()));               //"GET /movies"
        cmdTree.add(new CommandBuilder(new GetMovieID()));              //"GET /movies/{mid}"


        /* Commands related to CINEMAS */
        cmdTree.add(new CommandBuilder(new PostCinemas()));             //"POST /cinemas"
        cmdTree.add(new CommandBuilder(new GetCinemas()));              //"GET /cinemas"
        cmdTree.add(new CommandBuilder(new GetCinemaID()));             //"GET /cinemas/{cid}"


        /* Commands related to CINEMAS->THEATERS */
        cmdTree.add(new CommandBuilder(new PostCinemaIDTheaters()));    //"POST /cinemas/{cid}/theaters"
        cmdTree.add(new CommandBuilder(new GetCinemaIDTheaters()));     //"GET /cinemas/{cid}/theaters"
        cmdTree.add(new CommandBuilder(new GetCinemaIDTheatersID()));   //"GET /cinemas/{cid}/theaters/{tid}"


        /* Commands related to CINEMAS->THEATERS->SESSIONS */
        cmdTree.add(new CommandBuilder(new PostCinemaIDTheaterIDSessions()));       //"POST /cinemas/{cid}/theaters/{tid}/sessions"
        cmdTree.add(new CommandBuilder(new GetCinemaIDTheaterIDSessions()));        //"GET /cinemas/{cid}/theaters/{tid}/sessions"
        cmdTree.add(new CommandBuilder(new GetCinemaIDTheaterIDSessionsToday()));   //"GET /cinemas/{cid}/theaters/{tid}/sessions/today"


        /* Commands related to CINEMAS->SESSIONS */
        cmdTree.add(new CommandBuilder(new GetCinemaIDSessions()));      //"GET /cinemas/{cid}/sessions"
        cmdTree.add(new CommandBuilder(new GetCinemaIDSessionID()));     //"GET /cinemas/{cid}/sessions/{sid}"
        cmdTree.add(new CommandBuilder(new GetCinemaIDSessionsToday())); //"GET /cinemas/{cid}/sessions/today"


        /* Commands related to Tickets */
        cmdTree.add(new CommandBuilder(new PostCinemaIDTheaterIDSessionIDTickets()));           //"POST /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdTree.add(new CommandBuilder(new GetCinemaIDTheaterIDSessionIDTickets()));            //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdTree.add(new CommandBuilder(new GetCinemaIDTheaterIDSessionIDTicketID()));           //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid}"
        cmdTree.add(new CommandBuilder(new GetCinemaIDTheaterIDSessionIDTicketsAvailable()));   //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available"
        cmdTree.add(new CommandBuilder(new DeleteCinemaIDTheaterIDSessionIDTicket()));          //"DELETE /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdTree.add(new CommandBuilder(new GetCinemaIDSessionsDateID()));                       //"GET /cinemas/{cid}/sessions/date/{dmy}"
        cmdTree.add(new CommandBuilder(new GetMovieIDSessionsDateID()));                        //"GET /movies/{mid}/sessions/date/{dmy}"
    }

    /**
     * Fills the HashMap of Directory->ID.
     * This HashMap is used to know which current directory its at when we find a INT value,
     * there we replace that value with the value thats in this HashMap, so the commandTree can
     * continue its way in the directories.
     */
    private void initializeDirID() {
        dirID.put(CINEMAS.toString(),  ""+ID_PREFIX+CINEMA_ID+ID_SUFFIX);
        dirID.put(MOVIES.toString(),   ""+ID_PREFIX+MOVIE_ID+ID_SUFFIX);
        dirID.put(THEATERS.toString(), ""+ID_PREFIX+THEATER_ID+ID_SUFFIX);
        dirID.put(SESSIONS.toString(), ""+ID_PREFIX+SESSION_ID+ID_SUFFIX);
        dirID.put(DATE.toString(),     ""+ID_PREFIX+DATE_ID+ID_SUFFIX);
        dirID.put(TICKETS.toString(),  ""+ID_PREFIX+TICKET_ID+ID_SUFFIX);
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
        headersTree.add(new CommandBuilder(new Plain()));   //"PLAIN"
        headersTree.add(new CommandBuilder(new Html()));    //"HTML"

        /* Directory: application */
        headersTree.add(new CommandBuilder(new Json()));    //"JSON"
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
