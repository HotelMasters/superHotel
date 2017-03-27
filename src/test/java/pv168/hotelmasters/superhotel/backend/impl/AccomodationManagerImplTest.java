package pv168.hotelmasters.superhotel.backend.impl;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import javax.sql.DataSource;


import pv168.hotelmasters.superhotel.Exceptions.InvalidEntityException;
import pv168.hotelmasters.superhotel.backend.db.Utilities;
import pv168.hotelmasters.superhotel.backend.entities.Accommodation;
import pv168.hotelmasters.superhotel.backend.entities.Guest;
import pv168.hotelmasters.superhotel.backend.entities.Room;
import pv168.hotelmasters.superhotel.backend.exceptions.ValidationError;
import pv168.hotelmasters.superhotel.backend.impl.GuestFactory;
import pv168.hotelmasters.superhotel.backend.impl.RoomFactory;
import static org.assertj.core.api.Assertions.*;
import java.time.LocalDateTime;

import java.sql.SQLException;
import java.time.*;

import static java.time.Month.*;

/**
 * @author Gabriela Godiskova
 */
public class AccommodationManagerImplTest {

    private AccommodationManagerImpl manager;
    private GuestManagerImpl guestManager;
    private RoomManagerImpl roomManager;
    private DataSource dataSource;

    private final static ZonedDateTime NOW = LocalDateTime.of(2012,FEBRUARY,29,16,00).atZone(ZoneId.of("UTC"));

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static DataSource prepareDataSrc() throws SQLException {
        EmbeddedDataSource dataSrc = new EmbeddedDataSource();
        dataSrc.setDatabaseName("memory: superHotel-test");
        dataSrc.setCreateDatabase("create");
        return dataSrc;
    }

    @Before
    public void setUp() throws SQLException{
        dataSource = prepareDataSrc();
        Utilities.executeSql(getClass().getResource("createTables.sql"),dataSource);
        manager = new AccommodationManagerImpl();
        manager.setDataSource(dataSource);
        guestManager = new GuestManagerImpl(Clock.fixed(NOW.toInstant(),NOW.getZone()));
        guestManager.setDataSource(dataSource);
        roomManager = new RoomManagerImpl();
        roomManager.setDataSource(dataSource);
        prepareTestData();
    }

    @After
    public void tearDown() throws SQLException {
        Utilities.executeSql(getClass().getResource("dropTables.sql"),dataSource);
    }

    private Guest john,jane,jack,phoebe,jefrey,guestWithNullId,guestNotInDB;
    private Room economy,luxury,penthouse,roomWithNullId,roomNotInDB;

    public void prepareTestData() throws InvalidEntityException {
        john = new GuestFactory()
                .name("john")
                .address("Manesova 120, Brno")
                .birthday(LocalDate.of(1996,NOVEMBER,23))
                .crCardNm(Long.valueOf(1234)).build();
        jane = new GuestFactory()
                .name("jane")
                .address("Filkukova 42, Brno")
                .birthday(LocalDate.of(2008,FEBRUARY,29))
                .crCardNm(Long.valueOf(12345))
                .build();
        jack = new GuestFactory().name("jack")
                .address("Hrncirska 23, Brno")
                .birthday(LocalDate.of(1997,AUGUST,10))
                .crCardNm(Long.valueOf(1234567))
                .build();
        phoebe = new GuestFactory().name("phoebe")
                .address("5th Avenue 21, New York")
                .birthday(LocalDate.of(1974,DECEMBER,5))
                .crCardNm(Long.valueOf(12345678))
                .build();
        jefrey = new GuestFactory().name("jefrey")
                .address("Green Street 12, Springfield")
                .birthday(LocalDate.of(1987,MAY,30))
                .crCardNm(Long.valueOf(123456789))
                .build();
        economy = new RoomFactory().price(200.00).capacity(3).build();
        luxury = new RoomFactory().price(400.00).capacity(2).build();
        penthouse = new RoomFactory().price(2200.00).capacity(4).build();

        guestManager.createGuest(john);
        guestManager.createGuest(jane);
        guestManager.createGuest(jack);
        guestManager.createGuest(phoebe);
        guestManager.createGuest(jefrey);

        roomManager.createRoom(economy);
        roomManager.createRoom(luxury);
        roomManager.createRoom(penthouse);

        guestWithNullId = new GuestFactory().id(null).build();
        guestNotInDB = new GuestFactory().id(john.getId()+256).build();
        assertThat(guestManager.findGuestById(guestNotInDB.getId())).isNull();
    }

    @Test
    public void createAccommodation() {

        assertThat(manager.findRoomByGuest(john)).isNull();
        assertThat(manager.findRoomByGuest(jane)).isNull();
        assertThat(manager.findRoomByGuest(jack)).isNull();
        assertThat(manager.findRoomByGuest(phoebe)).isNull();
        assertThat(manager.findRoomByGuest(jefrey)).isNull();

        Accommodation acc1 = acc1Builder().build();
        Accommodation acc2 = acc2Builder().build();

        manager.createAccommodation(acc1);
        manager.createAccommodation(acc2);

        assertThat(manager.findGuestByRoom(economy)).isEqualToComparingFieldByField(john);
        assertThat(manager.findGuestByRoom(luxury)).isEqualToComparingFieldByField(jane);
        assertThat(manager.findGuestByRoom(penthouse)).isNull();

        assertThat(manager.findRoomByGuest(phoebe)).isNull();
        assertThat(manager.findRoomByGuest(jefrey)).isNull();
        assertThat(manager.findRoomByGuest(jack)).isNull();
        assertThat(manager.findRoomByGuest(john)).isEqualToComparingFieldByField(economy);
        assertThat(manager.findRoomByGuest(jane)).isEqualToComparingFieldByField(luxury);
    }

    @Test
    public void createAccommodationWithNullGuest(){
        Accommodation acc = acc1Builder().guest(null).build();
        expectedException.expect(IllegalArgumentException.class);
        manager.createAccommodation(acc);
    }

    @Test
    public void createAccommodationWithNullRoom() {
        Accommodation acc = acc1Builder().room(null).build();
        expectedException.expect(IllegalArgumentException.class);
        manager.createAccommodation(acc);
    }

    @Test
    public void createAccommodationWithExistingId() {
        Accommodation acc = acc1Builder().id(42L).build();
        expectedException.expect(InvalidEntityException.class);
        manager.createAccommodation(acc);
    }

    @Test
    public void deleteAccommodation() {
        Accommodation acc1 = acc1Builder().build();
        Accommodation acc2 = acc2Builder().build();

        manager.createAccommodation(acc1);
        manager.createAccommodation(acc2);

        assertThat(manager.findRoomByGuest(john)).isEqualToComparingFieldByField(economy);
        assertThat(manager.findRoomByGuest(jane)).isEqualToComparingFieldByField(luxury);
        assertThat(manager.findRoomByGuest(jack)).isNull();
        assertThat(manager.findRoomByGuest(jefrey)).isNull();
        assertThat(manager.findRoomByGuest(phoebe)).isNull();

        manager.deleteAccommodation(acc1);

        assertThat(manager.findAccommodationById(acc1.getId())).isNull();
        assertThat(manager.findAccommodationById(acc2.getId())).isEqualTo(acc2);
    }

    @Test
    public void deleteAccommodationWithNullId() {
        Accommodation acc1 = acc1Builder().id(null).build();
        manager.createAccommodation(acc1);
        expectedException.expect(IllegalArgumentException.class);
        manager.deleteAccommodation(acc1);
    }

    @Test
    public void deleteAccommodationWithNullRoom() {
        Accommodation acc2 = acc2Builder().room(null).build();
        manager.createAccommodation(acc2);
        expectedException.expect(IllegalArgumentException.class);
        manager.deleteAccommodation(acc2);
    }

    @Test
    public void deleteAccommodationWithNullGuest() {
        Accommodation acc1 = acc1Builder().guest(null).build();
        manager.createAccommodation(acc1);
        expectedException.expect(IllegalArgumentException.class);
        manager.deleteAccommodation(acc1);
    }

    @Test
    public void updateAccommodation() {
        Accommodation acc1 = acc1Builder().build();
        manager.createAccommodation(acc1);

        Accommodation acc2 = acc2Builder().build();
        manager.createAccommodation(acc2);

        Long acc1Id = acc1.getId();

        acc1 = manager.findAccommodationById(acc1Id);
        acc1.setGuest(jack);
        manager.updateAccommodation(acc1);
        assertThat(manager.findAccommodationById(acc1.getId())).isEqualToComparingFieldByField(acc1);

        acc1 = manager.findAccommodationById(acc1Id);
        acc1.setRoom(penthouse);
        manager.updateAccommodation(acc1);
        assertThat(manager.findAccommodationById(acc1.getId())).isEqualToComparingFieldByField(acc1);

        acc1 = manager.findAccommodationById(acc1Id);
        acc1.setDateFrom(LocalDateTime.of(2008,FEBRUARY,29,13,00));
        manager.updateAccommodation(acc1);
        assertThat(manager.findAccommodationById(acc1.getId())).isEqualToComparingFieldByField(acc1);

        acc1 = manager.findAccommodationById(acc1Id);
        acc1.setDateTo(LocalDateTime.of(2016,APRIL,4,12,00));
        manager.updateAccommodation(acc1);
        assertThat(manager.findAccommodationById(acc1.getId())).isEqualToComparingFieldByField(acc1);

        acc1 = manager.findAccommodationById(acc1Id);
        acc1.setTotalPrice(201.00);
        manager.updateAccommodation(acc1);
        assertThat(manager.findAccommodationById(acc1.getId())).isEqualToComparingFieldByField(acc1);

        assertThat(manager.findAccommodationById(acc2.getId())).isEqualToComparingFieldByField(acc2);
    }

    @Test
    public void updateAccommodationWithNullId() {
        Accommodation acc1 = acc1Builder().build();
        manager.createAccommodation(acc1);
        acc1.setId(null);
        expectedException.expect(InvalidEntityException.class);
        manager.updateAccommodation(acc1);
    }

    @Test
    public void updateAccommodationWithNullGuest() {
        Accommodation acc1 = acc1Builder().build();
        manager.createAccommodation(acc1);
        acc1.setGuest(null);
        expectedException.expect(ValidationError.class);
        manager.updateAccommodation(acc1);
    }

    @Test
    public void updateAccommodationWithNullRoom() {
        Accommodation acc2 = acc2Builder().build();
        manager.createAccommodation(acc2);
        acc2.setRoom(null);
        expectedException.expect(ValidationError.class);
        manager.updateAccommodation(acc2);
    }



    public AccommodationFactory acc1Builder() {
        return new AccommodationFactory().guest(john)
                .dateFrom(LocalDateTime.of(2016,FEBRUARY,29,12,00))
                .dateTo(LocalDateTime.of(2016,MARCH,1,10,00))
                .room(economy)
                .totalPrice(200.00);
    }

    public AccommodationFactory acc2Builder() {
        return new AccommodationFactory().guest(jane)
                .dateFrom(LocalDateTime.of(2017,JANUARY,21,12,00))
                .dateTo(LocalDateTime.of(2017,JANUARY,23,10,00))
                .room(luxury)
                .totalPrice(400.00);
    }


}
