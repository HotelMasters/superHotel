package pv168.hotelmasters.superhotel.backend.interfaces;

import pv168.hotelmasters.superhotel.backend.entities.Accommodation;
import pv168.hotelmasters.superhotel.backend.entities.Guest;
import pv168.hotelmasters.superhotel.backend.entities.Room;

import java.util.List;

/**
 * @author 445434
 */
public interface AccommodationManager {

    void createAccommodation(Accommodation accommodation);
    void updateAccommodation(Accommodation accommodation);
    void deleteAccommodation(Accommodation accommodation);
    Room findRoomByGuest(Guest guest);
    Guest findGuestByRoom(Room room);
    Accommodation findAccommodationById(Long accommodationId);
    List<Accommodation> findAllAccommodations();

}
