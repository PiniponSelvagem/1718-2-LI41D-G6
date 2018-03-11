package pt.isel.ls.command.utils;

import pt.isel.ls.command.*;

import java.util.HashMap;

import static pt.isel.ls.command.strings.CommandEnum.*;

public class CommandUtils {

    private String root = String.valueOf(ROOT_DIR);
    private CommandTree cmdTree = new CommandTree(new CommandNode(root));
    private HashMap<String, String> dirID = new HashMap<>();
    private HashMap<String, Boolean> paramsCheck = new HashMap<>();

    public CommandUtils() {
        initializeTree();
        initializeDirID();
        initializeParamsCheck();
        System.out.println(ROOT_DIR);
    }


    private void initializeTree() {
        /* Commands related to MOVIES */
        //"POST /movies"
        cmdTree.add(new CommandBuilder(""+
                POST+ARGS_SEPERATOR+DIR_SEPARATOR+MOVIES,
                new PostMovies())
        );
        //"GET /movies"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+MOVIES,
                new GetMovies())
        );
        //"GET /movies/{mid}"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+MOVIES+DIR_SEPARATOR+ID_PREFIX+MOVIES_ID+ID_SUFFIX,
                new GetMovieID())
        );


        /* Commands related to CINEMAS */
        //"POST /cinemas"
        cmdTree.add(new CommandBuilder(""+
                POST+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS,
                new PostCinemas())
        );
        //"GET /cinemas"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS,
                new GetCinemas())
        );
        //"GET /cinemas/{cid}"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX,
                new GetCinemaID())
        );


        /* Commands related to CINEMAS->THEATERS */
        //"POST /cinemas/{cid}/theaters"
        cmdTree.add(new CommandBuilder(""+
                POST+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS,
                new PostCinemaIDTheaters())
        );
        //"GET /cinemas/{cid}/theaters"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS,
                new GetCinemaIDTheaters())
        );
        //"GET /cinemas/{cid}/theaters/{tid}"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATERS_ID+ID_SUFFIX,
                new GetCinemaIDTheatersID())
        );


        /* Commands related to CINEMAS->THEATERS->SESSIONS */
        //"POST /cinemas/{cid}/theaters/{tid}/sessions"
        cmdTree.add(new CommandBuilder(""+
                POST+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATERS_ID+ID_SUFFIX+DIR_SEPARATOR+SESSIONS,
                new PostCinemaIDTheaterIDSessions())
        );
        //"GET /cinemas/{cid}/theaters/{tid}/sessions"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATERS_ID+ID_SUFFIX+DIR_SEPARATOR+SESSIONS,
                new GetCinemaIDTheaterIDSessions())
        );
        //"GET /cinemas/{cid}/theaters/{tid}/sessions/today"
        cmdTree.add(new CommandBuilder(""+
                POST+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX
                +DIR_SEPARATOR+THEATERS+DIR_SEPARATOR+ID_PREFIX+THEATERS_ID+ID_SUFFIX+DIR_SEPARATOR+SESSIONS
                +DIR_SEPARATOR+TODAY,
                new GetCinemaIDTheaterIDSessionsToday())
        );


        /* Commands related to CINEMAS->SESSIONS */
        //"GET /cinemas/{cid}/sessions"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX
                +DIR_SEPARATOR+SESSIONS,
                new GetCinemaIDSessions())
        );
        //"GET /cinemas/{cid}/sessions/{sid}"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX
                +SESSIONS+DIR_SEPARATOR+ID_PREFIX+SESSIONS_ID+ID_SUFFIX,
                new GetCinemaIDSessionID())
        );
        //"GET /cinemas/{cid}/sessions/today"
        cmdTree.add(new CommandBuilder(""+
                GET+ARGS_SEPERATOR+DIR_SEPARATOR+CINEMAS+DIR_SEPARATOR+ID_PREFIX+CINEMAS_ID+ID_SUFFIX
                +DIR_SEPARATOR+SESSIONS+DIR_SEPARATOR+TODAY,
                new GetCinemaIDSessionsToday())
        );
    }

    private void initializeDirID() {
        dirID.put(String.valueOf(CINEMAS),  String.valueOf(ID_PREFIX)+String.valueOf(CINEMAS_ID)+String.valueOf(ID_SUFFIX));
        dirID.put(String.valueOf(MOVIES),   String.valueOf(ID_PREFIX)+String.valueOf(MOVIES_ID)+String.valueOf(ID_SUFFIX));
        dirID.put(String.valueOf(THEATERS), String.valueOf(ID_PREFIX)+String.valueOf(THEATERS_ID)+String.valueOf(ID_SUFFIX));
        dirID.put(String.valueOf(SESSIONS), String.valueOf(ID_PREFIX)+String.valueOf(SESSIONS_ID)+String.valueOf(ID_SUFFIX));
    }

    private void initializeParamsCheck() {
        putParamsCheck(String.valueOf(CINEMAS_ID));
        putParamsCheck(String.valueOf(NAME));
        putParamsCheck(String.valueOf(CITY));

        putParamsCheck(String.valueOf(MOVIES_ID));
        putParamsCheck(String.valueOf(TITLE));
        putParamsCheck(String.valueOf(YEAR));
        putParamsCheck(String.valueOf(DURATION));

        putParamsCheck(String.valueOf(THEATERS_ID));
        putParamsCheck(String.valueOf(ROWS));
        putParamsCheck(String.valueOf(SEATS_ROW));

        putParamsCheck(String.valueOf(SESSIONS_ID));
        putParamsCheck(String.valueOf(DATE));
    }

    private void putParamsCheck(String param) {
        paramsCheck.put(param, true);
    }


    public CommandTree getCmdTree() {
        return cmdTree;
    }

    public String getRootName() {
        return root;
    }

    public String getDirID(String dir) {
        return dirID.get(dir);
    }

    public boolean validParam(String param) {
        return paramsCheck.get(param);
    }
}
