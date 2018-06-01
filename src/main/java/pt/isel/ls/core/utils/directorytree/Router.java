package pt.isel.ls.core.utils.directorytree;

import pt.isel.ls.core.common.commands.Command;
import pt.isel.ls.core.common.commands.NotFound;
import pt.isel.ls.core.utils.CommandBuilder;
import pt.isel.ls.core.utils.CommandUtils;

import java.util.HashMap;
import java.util.LinkedList;

public class Router {

    private CommandUtils cmdUtils;
    private DirectoryNode currDir;
    private DirectoryNode prevDir;

    private CommandBuilder cmdBuilder;
    private HashMap<String, String> ids = new HashMap<>();

    public Router(CommandBuilder cmdBuilder) {
        this.cmdBuilder = cmdBuilder;
        cmdUtils = cmdBuilder.getCmdUtils();
        currDir = cmdUtils.getCmdTree().getRoot();
    }

    /**
     * Search for the Command built from input.
     * Updates the CommandBuilder IDs HashMap.
     * @return Command
     */
    public Command searchCommand() {
        Command cmd = (Command) search(cmdBuilder.getPath(), cmdBuilder.getMethodName());
        cmdBuilder.setIds(ids);     //update CommandBuilder IDs HashMap
        return cmd;
    }


    /**
     * Search for the command.
     * @param path Search on this path.
     * @param methodName Method to search for.
     * @return If found return it, if not return {@link NotFound}
     */
    private Object search(LinkedList<String> path, String methodName) {
        if (path.isEmpty()) {
            return currDir.getMethods().get(methodName);
        } else {
            prevDir = currDir;
            findIdsAndReplace(path.getFirst());
            DirectoryNode currentChild = new DirectoryNode(path.pop());
            int index = currDir.getChilds().indexOf( currentChild );
            if ( index == -1 ) {
                return null;
            } else {
                currDir = currDir.getChilds().get(index);
                return search(path, methodName);
            }
        }
    }

    /**
     * If the previous dir has current dir, return and do nothing.
     * Else, set current dir has a ID.
     * Example: currDir = 12, prevDir = cinemas; < /cinemas/12 >
     *          Will ask to CommandUtils which KEY is used to store de ID for cinemas,
     *          it will return "{cid}" and only then add to the IDs HashMap
     *          Key= "{cid}" Value= 12
     * @param currDirName Current Directory
     */
    private void findIdsAndReplace(String currDirName) {
        if (prevDir.childsContains(currDirName) || prevDir == null)
            return;
        String prevDirName = cmdUtils.getDirID(prevDir.getDir());
        ids.put(prevDirName, currDirName);
        cmdBuilder.getPath().set(0, prevDirName);   //change the first item in the path to updated one, aka replace
    }
}
