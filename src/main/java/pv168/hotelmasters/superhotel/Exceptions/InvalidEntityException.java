package pv168.hotelmasters.superhotel.Exceptions;

import pv168.hotelmasters.superhotel.backend.exceptions.ManagerException;

/**
 * @author Gabriela Godiskova
 */
public class InvalidEntityException extends ManagerException {

    public InvalidEntityException(String msg) {
        super(msg);
    }
}
