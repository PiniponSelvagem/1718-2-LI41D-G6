package pt.isel.ls.command.utils;

import pt.isel.ls.command.*;

import java.util.HashMap;

public class CommandUtils {

    private String root = "root";
    private CommandTree cmdTree = new CommandTree(new CommandNode(root));
    private HashMap<String, String> dirID = new HashMap<>();

    public CommandUtils() {
        initializeTree();
        initializeDirID();
    }


    private void initializeTree() {
        cmdTree.add(new CommandBuilder("POST /movies/", new PostMovies()));
        cmdTree.add(new CommandBuilder("GET /movies/", new GetMovies()));
        cmdTree.add(new CommandBuilder("GET /movies/{mid}/", new GetMovieID()));

        cmdTree.add(new CommandBuilder("POST /cinemas/", new PostCinemas()));
        cmdTree.add(new CommandBuilder("GET /cinemas/", new GetCinemas()));
        cmdTree.add(new CommandBuilder("GET /cinemas/{cid}/", new GetCinemaID()));

        cmdTree.add(new CommandBuilder("POST /cinemas/{cid}/theaters/", new PostCinemaIDTheaters()));
        cmdTree.add(new CommandBuilder("GET /cinemas/{cid}/theaters/", new GetCinemaIDTheaters()));
        cmdTree.add(new CommandBuilder("GET /cinemas/{cid}/theaters/{tid}/", new GetCinemaIDTheatersID()));

        cmdTree.add(new CommandBuilder("POST /cinemas/{cid}/theaters/{tid}/sessions", new PostCinemaIDTheaterIDSessions()));
        cmdTree.add(new CommandBuilder("GET /cinemas/{cid}/theaters/{tid}/sessions", new GetCinemaIDTheaterIDSessions()));
        cmdTree.add(new CommandBuilder("GET /cinemas/{cid}/theaters/{tid}/sessions/today", new GetCinemaIDTheaterIDSessionsToday()));

        cmdTree.add(new CommandBuilder("GET /cinemas/{cid}/sessions", new GetCinemaIDSessions()));
        cmdTree.add(new CommandBuilder("GET /cinemas/{cid}/sessions/{sid}/", new GetCinemaIDSessionID()));
        cmdTree.add(new CommandBuilder("GET /cinemas/{cid}/sessions/today", new GetCinemaIDSessionsToday()));
    }

    private void initializeDirID() {
        dirID.put("cinemas",  "{cid}");
        dirID.put("movies",   "{mid}");
        dirID.put("theaters", "{tid}");
        dirID.put("sessions", "{sid}");
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
}
