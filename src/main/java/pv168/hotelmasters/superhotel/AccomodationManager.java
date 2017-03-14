package pv168.hotelmasters.superhotel;

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
