package pv168.hotelmasters.superhotel.exceptions;

/**
 * @author Kristian Lesko
 */
public class ValidationError extends ManagerException {
    public ValidationError(String msg) {
        super(msg);
    }

    public ValidationError(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ValidationError(Throwable cause) {
        super(cause);
    }
}
