import java.util.List;

/**
 * @author 445434
 */
public interface AccomodationManager {

    void createAccomodation(Accomodation accomodation);
    void updateAccomodation(Accomodation accomodation);
    void deleteAccomodation(Accomodation accomodation);
    Room findRoomByGuest(Guest guest);
    Guest findGuestByRoom(Room room);
    Accomodation findAccomodationById(Accomodation accomodation);
    List<Accomodation> findAllAccomodations();

}
