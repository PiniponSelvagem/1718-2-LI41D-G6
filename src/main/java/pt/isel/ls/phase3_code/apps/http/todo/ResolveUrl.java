package pt.isel.ls.phase3_code.apps.http.todo;

import pt.isel.ls.phase3_code.todo.ToDo;

public class ResolveUrl {

    public static String of(ToDo td) {
        return String.format("/todos/%d", td.getId());
    }
    
    public static String ofToDoCollection() {
        return "/todos";
    }    
}
