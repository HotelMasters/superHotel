package pv168.hotelmasters.superhotel;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * @author Gabriela Godiskova
 */
public class GuestManagerImplTest {

    private GuestManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new GuestManagerImpl();
    }

    @Test
    public void createGuest() throws Exception {
        Guest guest = newGuest("John Locke", "Kvetna 42, Brno-Pisarky", LocalDate.of(1962, 8, 29), Long.valueOf(12345678));
        manager.createGuest(guest);
        Long guestId = guest.getId();
        assertNotNull(guestId);
        Guest result = manager.findGuestById(guestId);
        assertEquals(guest, result);
        assertNotSame(guest, result);
        assertDeepEquals(guest, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGuestWithNullName() {
        Guest guest = newGuest(null,"Filkukova 256, Brno-Reckovice",LocalDate.of(1997,8,10),Long.valueOf(123456));
        manager.createGuest(guest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGuestWithNullAdress() {
        Guest guest = newGuest("Adam Smith",null,LocalDate.of(1963,6,16),Long.valueOf(1234567));
        manager.createGuest(guest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullGuest() {
        manager.createGuest(null);
    }



    @Test
    public void updateGuest() throws Exception {
        Guest guest = newGuest("John Locke", "Kvetna 42, Brno-Pisarky", LocalDate.of(1962, 8, 29), Long.valueOf(12345678));
        Guest difGuest = newGuest("Adam Smith","Filkukova 256, Brno-Reckovice",LocalDate.of(1963,6,16),Long.valueOf(1234567));
        manager.createGuest(guest);
        manager.createGuest(difGuest);
        Long guestId = guest.getId();

        guest = manager.findGuestById(guestId);
        guest.setName("Henry Bacon");
        manager.updateGuest(guest);
        assertEquals("Henry Bacon",guest.getName());
        assertEquals("Kvetna 42, Brno-Pisarky",guest.getAdress());
        assertEquals(LocalDate.of(1962, 8, 29),guest.getBirthDay());
        assertEquals(Long.valueOf(12345678),guest.getCrCardNumber());

        guest = manager.findGuestById(guestId);
        guest.setAdress("Manesova 21,Brno-Kralovo Pole");
        manager.updateGuest(guest);
        assertEquals("Henry Bacon",guest.getName());
        assertEquals("Manesova 21,Brno-Kralovo Pole",guest.getAdress());
        assertEquals(LocalDate.of(1962, 8, 29),guest.getBirthDay());
        assertEquals(Long.valueOf(12345678),guest.getCrCardNumber());

        guest = manager.findGuestById(guestId);
        guest.setCrCardNumber(Long.valueOf(123456789));
        manager.updateGuest(guest);
        assertEquals("Henry Bacon",guest.getName());
        assertEquals("Manesova 21,Brno-Kralovo Pole",guest.getAdress());
        assertEquals(LocalDate.of(1962, 8, 29),guest.getBirthDay());
        assertEquals(Long.valueOf(123456789),guest.getCrCardNumber());

        assertDeepEquals(difGuest,manager.findGuestById(difGuest.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullGuest() throws Exception{
        manager.updateGuest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateGuestWithNullName() {
        Guest guest = newGuest("John Locke", "Kvetna 42, Brno-Pisarky", LocalDate.of(1962, 8, 29), Long.valueOf(12345678));
        Long guestId = guest.getId();
        guest = manager.findGuestById(guestId);
        guest.setName(null);
        manager.updateGuest(guest);
    }

    @Test
    public void deleteGuest() throws Exception {
        Guest guest = newGuest("John Locke", "Kvetna 42, Brno-Pisarky", LocalDate.of(1962, 8, 29), Long.valueOf(12345678));
        Guest difGuest = newGuest("Adam Smith","Filkukova 256, Brno-Reckovice",LocalDate.of(1963,6,16),Long.valueOf(1234567));
        manager.createGuest(guest);
        manager.createGuest(difGuest);

        assertNotNull(manager.findGuestById(guest.getId()));
        assertNotNull(manager.findGuestById(difGuest.getId()));

        manager.deleteGuest(guest);

        assertNull(manager.findGuestById(guest.getId()));
        assertNotNull(manager.findGuestById(difGuest.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullGuest() {
        manager.deleteGuest(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteGuestWithNullId(){
        Guest guest = newGuest("John Locke", "Kvetna 42, Brno-Pisarky", LocalDate.of(1962, 8, 29), Long.valueOf(12345678));
        guest.setId(null);
        manager.deleteGuest(guest);
    }


    private static Guest newGuest(String name, String adress, LocalDate birthday, Long crCardNumber) {
        Guest guest = new Guest();
        guest.setName(name);
        guest.setAdress(adress);
        guest.setBirthDay(birthday);
        guest.setCrCardNumber(crCardNumber);
        return guest;
    }

    private void assertDeepEquals(Guest expected, Guest actual){
        assertEquals(expected.getId(),actual.getId());
        assertEquals(expected.getName(),actual.getName());
        assertEquals(expected.getAdress(),actual.getAdress());
        assertEquals(expected.getBirthDay(),actual.getBirthDay());
        assertEquals(expected.getCrCardNumber(),actual.getCrCardNumber());
    }

}