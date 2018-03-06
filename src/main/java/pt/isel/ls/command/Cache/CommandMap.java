package pt.isel.ls.command.Cache;

import pt.isel.ls.command.*;

import java.util.HashMap;

public class CommandMap {

    /* ---------------- WARNING ----------------
     * THIS CLASS HAS TEMPORARY CODE THAT SHOULD
     * BE REPLACED LATER ON.
     *
     * TODO: replace HashMap with custom implementation.
     */

    private static HashMap<String, Command> commandMap = new HashMap<>();

    public static void mapInitialize() {
        commandMap.put("GET /movies", new GetMovies());
        commandMap.put("POST /movies", new PostMovies());
    }

    public static Command mapSearch(String str) {
        Command command = commandMap.get(str);
        if (command==null)
            return new NotFound();
        return command;
    }
}
