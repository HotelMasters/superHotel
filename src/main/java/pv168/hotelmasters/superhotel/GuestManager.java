package pv168.hotelmasters.superhotel;

import pv168.hotelmasters.superhotel.Exceptions.InvalidEntityException;

import javax.xml.bind.ValidationException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 445434
 */
public interface GuestManager {

    void createGuest(Guest guest) throws ValidationException, InvalidEntityException, SQLException;
    void updateGuest(Guest guest) throws InvalidEntityException;
    void deleteGuest(Guest guest) throws InvalidEntityException;
    Guest findGuestById(Long guestId);
    List<Guest> findAllGuests();
}
