package pt.isel.ls.phase3_code.apps.http.todo;

import pt.isel.ls.phase3_code.html.HtmlElem;
import pt.isel.ls.phase3_code.html.HtmlPage;
import pt.isel.ls.phase3_code.todo.ToDo;

import pt.isel.ls.phase3_code.common.Writable;

public class ToDosView extends HtmlPage{

    public ToDosView(Iterable<ToDo> list){
        super("To Do list",
            h1(text("To Do list")),            
            todoItems(list),
            h1(text("Create new To Do")),
            form("POST", "/todos",
                label("description","Description: "),
                textInput("description")
            )
        );        
    }
    
    private static Writable todoItems(Iterable<ToDo> list) {
        HtmlElem ul = new HtmlElem("ul");
        for(ToDo todo : list){
            ul.withContent(
                li(a(ResolveUrl.of(todo),todo.toString()))
            );
        }
        return ul;
    }    
}
