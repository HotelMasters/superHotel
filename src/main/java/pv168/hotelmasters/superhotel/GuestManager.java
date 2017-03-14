package pv168.hotelmasters.superhotel;

import java.util.List;

/**
 * @author 445434
 */
public interface GuestManager {

    void createGuest(Guest guest);
    void updateGuest(Guest guest);
    void deleteGuest(Guest guest);
    Guest findGuestById(Long guestId);
    List<Guest> findAllGuests();
}
