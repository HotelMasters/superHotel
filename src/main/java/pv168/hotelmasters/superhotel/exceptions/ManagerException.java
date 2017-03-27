package pv168.hotelmasters.superhotel.exceptions;

/**
 * @author Kristian Lesko
 */
public class ManagerException extends RuntimeException {
    public ManagerException(String msg) {
        super(msg);
    }

    public ManagerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ManagerException(Throwable cause) {
        super(cause);
    }
}
