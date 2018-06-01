package pt.isel.ls.core.exceptions;

import pt.isel.ls.core.strings.ExceptionEnum;

public class ViewNotImplementedException extends CommonException {

    public ViewNotImplementedException(ExceptionEnum msg) {
        this.msg = msg.toString();
    }

    public ViewNotImplementedException(String msg) {
        this.msg = "Could not create view for requested header type <"+msg+">.";
    }
}
