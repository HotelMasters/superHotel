package pv168.hotelmasters.superhotel.backend.interfaces;

import pv168.hotelmasters.superhotel.backend.entities.Accommodation;
import pv168.hotelmasters.superhotel.backend.entities.Guest;
import pv168.hotelmasters.superhotel.backend.entities.Room;

import java.util.List;

/**
 * @author 445434
 */
public interface AccomodationManager {

    void createAccomodation(Accommodation accommodation);
    void updateAccomodation(Accommodation accommodation);
    void deleteAccomodation(Accommodation accommodation);
    Room findRoomByGuest(Guest guest);
    Guest findGuestByRoom(Room room);
    Accommodation findAccomodationById(Accommodation accommodation);
    List<Accommodation> findAllAccomodations();

}
