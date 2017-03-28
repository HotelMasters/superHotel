package pv168.hotelmasters.superhotel.backend.impl;

import pv168.hotelmasters.superhotel.backend.interfaces.AccomodationManager;
import pv168.hotelmasters.superhotel.backend.entities.Accommodation;
import pv168.hotelmasters.superhotel.backend.entities.Guest;
import pv168.hotelmasters.superhotel.backend.entities.Room;

import java.util.List;

/**
 * @author 445434
 */
public class AccomodationManagerImpl implements AccomodationManager {
    @Override
    public void createAccomodation(Accommodation accommodation) {

    }

    @Override
    public void updateAccomodation(Accommodation accommodation) {

    }

    @Override
    public void deleteAccomodation(Accommodation accommodation) {

    }

    @Override
    public Room findRoomByGuest(Guest guest) {
        return null;
    }

    @Override
    public Guest findGuestByRoom(Room room) {
        return null;
    }

    @Override
    public Accommodation findAccomodationById(Long id) {
        return null;
    }

    @Override
    public List<Accommodation> findAllAccomodations() {
        return null;
    }
}
