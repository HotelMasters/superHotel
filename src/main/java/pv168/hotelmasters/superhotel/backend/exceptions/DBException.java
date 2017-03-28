package pv168.hotelmasters.superhotel.backend.exceptions;

/**
 * @author Kristian Lesko
 */
public class DBException extends ManagerException {
    public DBException(String msg) {
        super(msg);
    }

    public DBException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DBException(Throwable cause) {
        super(cause);
    }
}
