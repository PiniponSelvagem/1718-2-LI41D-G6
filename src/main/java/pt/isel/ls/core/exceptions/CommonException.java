package pt.isel.ls.core.exceptions;

public abstract class CommonException extends Exception {

    String msg;

    @Override
    public final String getMessage() {
        return this.getClass().getSimpleName()+": "+msg;
    }
}
