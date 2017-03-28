package pv168.hotelmasters.superhotel.backend.interfaces;

import pv168.hotelmasters.superhotel.backend.entities.Guest;

import pv168.hotelmasters.superhotel.backend.exceptions.InvalidEntityException;
import pv168.hotelmasters.superhotel.backend.exceptions.ValidationError;

import java.sql.SQLException;
import java.util.List;

/**
 * @author 445434
 */
public interface GuestManager {

    void createGuest(Guest guest) throws ValidationError, SQLException;
    void updateGuest(Guest guest) throws InvalidEntityException;
    void deleteGuest(Guest guest) throws InvalidEntityException;
    Guest findGuestById(Long guestId);
    List<Guest> findAllGuests();
}
