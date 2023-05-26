package uos.cineseoul.exception;

public class DataInconsistencyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataInconsistencyException(String msg) {
        super(msg);
    }
}