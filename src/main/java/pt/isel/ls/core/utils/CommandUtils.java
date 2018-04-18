package pt.isel.ls.core.utils;

import pt.isel.ls.core.commands.*;
import pt.isel.ls.core.headers.*;
import pt.isel.ls.core.utils.directorytree.DirectoryNode;
import pt.isel.ls.core.utils.directorytree.DirectoryTree;

import java.util.HashMap;

import static pt.isel.ls.core.strings.CommandEnum.*;

public class CommandUtils {

    private String root = String.valueOf(ROOT_DIR);
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
        //"OPTIONS"
        cmdTree.add(new CommandBuilder(""+
                OPTIONS,
                null,
                new Options())
        );
        //"EXIT"
        cmdTree.add(new CommandBuilder(""+
                EXIT,
                null,
                new Exit())
        );

        /* Commands related to MOVIES */
        //"POST /movies"
        cmdTree.add(new CommandBuilder(""+
                POST, ""+
                DIR_SEPARATOR+MOVIES,
                new PostMovies())
        );
        //"GET /movies"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+MOVIES,
                new GetMovies())
        );
        //"GET /movies/{mid}"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+ID_PREFIX+MOVIE_ID+ID_SUFFIX,
                new GetMovieID())
        );


        /* Commands related to CINEMAS */
        //"POST /cinemas"
        cmdTree.add(new CommandBuilder(""+
                POST, ""+
                DIR_SEPARATOR+CINEMAS,
                new PostCinemas())
        );
        //"GET /cinemas"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS,
                new GetCinemas())
        );
        //"GET /cinemas/{cid}"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX,
                new GetCinemaID())
        );


        /* Commands related to CINEMAS->THEATERS */
        //"POST /cinemas/{cid}/theaters"
        cmdTree.add(new CommandBuilder(""+
                POST, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX+DIR_SEPARATOR+THEATERS,
                new PostCinemaIDTheaters())
        );
        //"GET /cinemas/{cid}/theaters"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX+DIR_SEPARATOR+THEATERS,
                new GetCinemaIDTheaters())
        );
        //"GET /cinemas/{cid}/theaters/{tid}"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX,
                new GetCinemaIDTheatersID())
        );


        /* Commands related to CINEMAS->THEATERS->SESSIONS */
        //"POST /cinemas/{cid}/theaters/{tid}/sessions"
        cmdTree.add(new CommandBuilder(""+
                POST, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX+DIR_SEPARATOR+SESSIONS,
                new PostCinemaIDTheaterIDSessions())
        );
        //"GET /cinemas/{cid}/theaters/{tid}/sessions"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX+DIR_SEPARATOR+SESSIONS,
                new GetCinemaIDTheaterIDSessions())
        );
        //"GET /cinemas/{cid}/theaters/{tid}/sessions/today"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX+DIR_SEPARATOR+SESSIONS
                    +DIR_SEPARATOR+TODAY,
                new GetCinemaIDTheaterIDSessionsToday())
        );


        /* Commands related to CINEMAS->SESSIONS */
        //"GET /cinemas/{cid}/sessions"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+SESSIONS,
                new GetCinemaIDSessions())
        );
        //"GET /cinemas/{cid}/sessions/{sid}"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX,
                new GetCinemaIDSessionID())
        );
        //"GET /cinemas/{cid}/sessions/today"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX+DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+TODAY,
                new GetCinemaIDSessionsToday())
        );


        /* Commands related to Tickets */
        //"POST /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdTree.add(new CommandBuilder(""+
                POST, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX
                    +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX
                    +DIR_SEPARATOR+TICKETS,
                new PostCinemaIDTheaterIDSessionIDTickets())
        );
        //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX
                    +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX
                    +DIR_SEPARATOR+TICKETS,
                new GetCinemaIDTheaterIDSessionIDTickets())
        );
        //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid}"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX
                    +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX
                    +DIR_SEPARATOR+TICKETS+DIR_SEPARATOR+ID_PREFIX+TICKET_ID+ID_SUFFIX,
                new GetCinemaIDTheaterIDSessionIDTicketID())
        );
        //"GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX
                    +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX
                    +DIR_SEPARATOR+TICKETS+DIR_SEPARATOR+AVAILABLE,
                new GetCinemaIDTheaterIDSessionIDTicketsAvailable())
        );
        //"DELETE /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets"
        cmdTree.add(new CommandBuilder(""+
                DELETE, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATER_ID+ID_SUFFIX
                    +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSION_ID+ID_SUFFIX
                    +DIR_SEPARATOR+TICKETS,
                new DeleteCinemaIDTheaterIDSessionIDTicket())
        );
        //"GET /cinemas/{cid}/sessions/date/{dmy}"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMA_ID+ID_SUFFIX
                    +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+DATE+DIR_SEPARATOR+ID_PREFIX+DATE_ID+ID_SUFFIX,
                new GetCinemaIDSessionsDateID())
        );
        //"GET /movies/{mid}/sessions/date/{d}"
        cmdTree.add(new CommandBuilder(""+
                GET, ""+
                DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+ID_PREFIX+MOVIE_ID+ID_SUFFIX
                    +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+DATE+DIR_SEPARATOR+ID_PREFIX+DATE_ID+ID_SUFFIX,
                new GetMovieIDSessionsDateID())
        );
    }

    /**
     * Fills the HashMap of Directory->ID.
     * This HashMap is used to know which current directory its at when we find a INT value,
     * there we replace that value with the value thats in this HashMap, so the commandTree can
     * continue its way in the directories.
     */
    private void initializeDirID() {
        dirID.put(String.valueOf(CINEMAS),  String.valueOf(ID_PREFIX)+String.valueOf(CINEMA_ID)+String.valueOf(ID_SUFFIX));
        dirID.put(String.valueOf(MOVIES),   String.valueOf(ID_PREFIX)+String.valueOf(MOVIE_ID)+String.valueOf(ID_SUFFIX));
        dirID.put(String.valueOf(THEATERS), String.valueOf(ID_PREFIX)+String.valueOf(THEATER_ID)+String.valueOf(ID_SUFFIX));
        dirID.put(String.valueOf(SESSIONS), String.valueOf(ID_PREFIX)+String.valueOf(SESSION_ID)+String.valueOf(ID_SUFFIX));
        dirID.put(String.valueOf(DATE), String.valueOf(ID_PREFIX)+String.valueOf(DATE_ID)+String.valueOf(ID_SUFFIX));
        dirID.put(String.valueOf(TICKETS), String.valueOf(ID_PREFIX)+String.valueOf(TICKET_ID)+String.valueOf(ID_SUFFIX));
    }

    /**
     * Fills the HashMap with the all available parameters, with value TRUE.
     * This way, when we can check if its a valid parameter with {@link #validParam(String param)}.
     */
    private void initializeParamsCheck() {
        putParamsCheck(String.valueOf(CINEMA_ID));
        putParamsCheck(String.valueOf(NAME));
        putParamsCheck(String.valueOf(CITY));

        putParamsCheck(String.valueOf(MOVIE_ID));
        putParamsCheck(String.valueOf(TITLE));
        putParamsCheck(String.valueOf(YEAR));
        putParamsCheck(String.valueOf(DURATION));

        putParamsCheck(String.valueOf(THEATER_ID));
        putParamsCheck(String.valueOf(ROWS));
        putParamsCheck(String.valueOf(SEATS_ROW));

        putParamsCheck(String.valueOf(SESSION_ID));
        putParamsCheck(String.valueOf(DATE_PARAM));
    }

    /**
     * Fills the headersTree with all the possible headers available.
     */
    private void initializeHeadersTree() {
        /* Directory: text */
        //"plain"
        headersTree.add(new CommandBuilder(""+
                PLAIN, ""+
                DIR_SEPARATOR+TEXT,
                new Plain())
        );
        //""
        headersTree.add(new CommandBuilder(""+
                HTML, ""+
                DIR_SEPARATOR+TEXT,
                new Html())
        );

        /* Directory: application */
        //"plain"
        headersTree.add(new CommandBuilder(""+
                JSON, ""+
                DIR_SEPARATOR+APPLICATION,
                new Json())
        );
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
        return paramsCheck.get(param);
    }
}
