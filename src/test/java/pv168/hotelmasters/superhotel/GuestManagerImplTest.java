package pv168.hotelmasters.superhotel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

/**
 * @author Gabriela Godiskova
 */
public class GuestManagerImplTest {

    private GuestManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new GuestManagerImpl();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createGuest() throws Exception {
        Guest guest = newGuest("John Locke", "Kvetna 42, Brno-Pisarky", LocalDate.of(1962, 8, 29), Long.valueOf(12345678));
        manager.createGuest(guest);
        Long guestId = guest.getId();
        assertThat(guestId).isNotNull();
        assertThat(manager.findGuestById(guestId))
                .isNotSameAs(guest)
                .isEqualToComparingFieldByField(guest);
    }


    public void createGuestWithNullName() {
        Guest guest = newGuest(null,"Filkukova 256, Brno-Reckovice",LocalDate.of(1997,8,10),Long.valueOf(123456));
        expectedException.expect(IllegalArgumentException.class);
        manager.createGuest(guest);
    }

    public void createGuestWithNullAdress() {
        Guest guest = newGuest("Adam Smith",null,LocalDate.of(1963,6,16),Long.valueOf(1234567));
        expectedException.expect(IllegalArgumentException.class);
        manager.createGuest(guest);
    }


    public void createNullGuest() {
        expectedException.expect(IllegalArgumentException.class);
        manager.createGuest(null);
    }


    public void createGuestWithNullBirthday() {
        Guest guest = newGuest("Henry Tudor", "Chestnut Ave 58 London", null, Long.valueOf(23456789));
        expectedException.expect(IllegalArgumentException.class);
        manager.createGuest(guest);
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
        assertThat(guest.getName()).isEqualTo("Henry Bacon");
        assertThat(guest.getAdress()).isEqualTo("Kvetna 42, Brno-Pisarky");
        assertThat(guest.getBirthDay()).isEqualTo(LocalDate.of(1962, 8, 29));
        assertThat(guest.getCrCardNumber()).isEqualTo(Long.valueOf(12345678));

        guest = manager.findGuestById(guestId);
        guest.setAdress("Manesova 21,Brno-Kralovo Pole");
        manager.updateGuest(guest);
        assertThat(guest.getName()).isEqualTo("Henry Bacon");
        assertThat(guest.getAdress()).isEqualTo("Manesova 21,Brno-Kralovo Pole");
        assertThat(guest.getBirthDay()).isEqualTo(LocalDate.of(1962, 8, 29));
        assertThat(guest.getCrCardNumber()).isEqualTo(Long.valueOf(12345678));

        guest = manager.findGuestById(guestId);
        guest.setCrCardNumber(Long.valueOf(123456789));
        manager.updateGuest(guest);
        assertThat(guest.getName()).isEqualTo("Henry Bacon");
        assertThat(guest.getAdress()).isEqualTo("Manesova 21,Brno-Kralovo Pole");
        assertThat(guest.getBirthDay()).isEqualTo(LocalDate.of(1962, 8, 29));
        assertThat(guest.getCrCardNumber()).isEqualTo(Long.valueOf(123456789));
    }


    public void updateNullGuest() throws Exception{
        expectedException.expect(IllegalArgumentException.class);
        manager.updateGuest(null);
    }


    public void updateGuestWithNullName() {
        Guest guest = newGuest("John Locke", "Kvetna 42, Brno-Pisarky", LocalDate.of(1962, 8, 29), Long.valueOf(12345678));
        Long guestId = guest.getId();
        guest = manager.findGuestById(guestId);
        guest.setName(null);
        expectedException.expect(IllegalArgumentException.class);
        manager.updateGuest(guest);
    }

    @Test
    public void deleteGuest() throws Exception {
        Guest john = newGuest("John Locke", "Kvetna 42, Brno-Pisarky", LocalDate.of(1962, 8, 29), Long.valueOf(12345678));
        Guest adam = newGuest("Adam Smith","Filkukova 256, Brno-Reckovice",LocalDate.of(1963,6,16),Long.valueOf(1234567));
        manager.createGuest(john);
        manager.createGuest(adam);

        assertThat(manager.findGuestById(john.getId())).isNotNull();
        assertThat(manager.findGuestById(adam.getId())).isNotNull();

        manager.deleteGuest(john);

        assertThat(manager.findGuestById(john.getId())).isNull();
        assertThat(manager.findGuestById(adam.getId())).isNotNull();
    }


    public void deleteNullGuest() {
        expectedException.expect(IllegalArgumentException.class);
        manager.deleteGuest(null);
    }


    public void deleteGuestWithNullId(){
        Guest guest = newGuest("John Locke", "Kvetna 42, Brno-Pisarky", LocalDate.of(1962, 8, 29), Long.valueOf(12345678));
        guest.setId(null);
        expectedException.expect(IllegalArgumentException.class);
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