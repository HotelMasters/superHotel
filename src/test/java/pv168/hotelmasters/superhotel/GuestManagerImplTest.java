package pv168.hotelmasters.superhotel;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pv168.hotelmasters.superhotel.Exceptions.InvalidEntityException;
import pv168.hotelmasters.superhotel.db.Utilities;
import pv168.hotelmasters.superhotel.exceptions.ValidationError;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.*;


import static java.time.Month.AUGUST;
import static java.time.Month.FEBRUARY;
import static java.time.Month.NOVEMBER;
import static org.assertj.core.api.Assertions.*;

/**
 * @author Gabriela Godiskova
 */
public class GuestManagerImplTest {

    private GuestManagerImpl manager;
    private DataSource dataSource;
    private final static ZonedDateTime NOW = LocalDateTime.of(2012, FEBRUARY, 29, 16, 00).atZone(ZoneId.of("UTC"));


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource datSrc = new EmbeddedDataSource();
        datSrc.setDatabaseName("memory:guestMngrTest");
        datSrc.setCreateDatabase("create");
        return datSrc;
    }

    private static Clock prepareClock(ZonedDateTime now) {
        return Clock.fixed(now.toInstant(), now.getZone());
    }

    @Before
    public void setUp() throws SQLException {
        dataSource = prepareDataSource();
        Utilities.executeSql(GuestManager.class.getResource("createTables.sql"),dataSource);
        manager = new GuestManagerImpl(prepareClock(NOW));
        manager.setDataSource(dataSource);
    }

    @After
    public void tearDown() throws SQLException{
        Utilities.executeSql(GuestManager.class.getResource("dropTables.sql"),dataSource);
    }


    private GuestBuilder johnBuilder() {
        return new GuestBuilder()
                .name("John Locke")
                .address("Kvetna 42, Brno")
                .birthday(1962,AUGUST,29)
                .crCardNm(Long.valueOf(12345678));
    }

    private GuestBuilder janeBuilder() {
        return new GuestBuilder()
                .name("Jane Eyre")
                .address("Chestnut Ave 102, London")
                .birthday(1989,NOVEMBER,17)
                .crCardNm(Long.valueOf(123456789));
    }


    @Test
    public void createGuest() throws InvalidEntityException,SQLException {

        Guest john = johnBuilder().build();
        manager.createGuest(john);

        Long guestId = john.getId();
        assertThat(guestId).isNotNull();
        assertThat(manager.findGuestById(guestId))
                .isNotSameAs(john)
                .isEqualToComparingFieldByField(john);
    }


    @Test
    public void createGuestWithNullName() throws InvalidEntityException,SQLException {
        Guest john = johnBuilder().name(null).build();
        expectedException.expect(ValidationError.class);
        manager.createGuest(john);
    }

    @Test
    public void createGuestWithNullAdress() throws InvalidEntityException,SQLException {
        Guest john = johnBuilder().address(null).build();
        expectedException.expect(ValidationError.class);
        manager.createGuest(john);
    }


    @Test
    public void createNullGuest() throws InvalidEntityException,SQLException {
        expectedException.expect(IllegalArgumentException.class);
        manager.createGuest(null);
    }


    @Test
    public void createGuestWithNullBirthday() throws InvalidEntityException {
        Guest john = johnBuilder().address(null).build();
        expectedException.expect(ValidationError.class);
        manager.createGuest(john);
    }



    @Test
    public void updateGuest() throws InvalidEntityException,SQLException{
        Guest john = johnBuilder().build();
        Guest jane = janeBuilder().build();
        manager.createGuest(john);
        manager.createGuest(jane);
        Long guestId = john.getId();

        john = manager.findGuestById(guestId);
        john.setName("Henry Bacon");
        manager.updateGuest(john);
        assertThat(manager.findGuestById(john.getId())).isEqualToComparingFieldByField(john);

        john = manager.findGuestById(guestId);
        john.setAdress("Manesova 21,Brno-Kralovo Pole");
        manager.updateGuest(john);
        assertThat(manager.findGuestById(john.getId())).isEqualToComparingFieldByField(john);


        john = manager.findGuestById(guestId);
        john.setCrCardNumber(Long.valueOf(123456789));
        manager.updateGuest(john);
        assertThat(manager.findGuestById(john.getId())).isEqualToComparingFieldByField(john);
        assertThat(manager.findGuestById(jane.getId())).isEqualToComparingFieldByField(jane);
    }


    @Test
    public void updateNullGuest() throws InvalidEntityException{
        expectedException.expect(IllegalArgumentException.class);
        manager.updateGuest(null);
    }

    @Test
    public void updateGuestWithNullName() throws InvalidEntityException {
        Guest john = johnBuilder().build();
        manager.createGuest(john);
        Long guestId = john.getId();

        john = manager.findGuestById(guestId);
        john.setName(null);
        expectedException.expect(ValidationError.class);
        manager.updateGuest(john);
    }

    @Test
    public void deleteGuest() throws InvalidEntityException,SQLException{
        Guest john = johnBuilder().build();
        Guest jane = janeBuilder().build();
        manager.createGuest(john);
        manager.createGuest(jane);

        assertThat(manager.findGuestById(john.getId())).isNotNull();
        assertThat(manager.findGuestById(jane.getId())).isNotNull();

        manager.deleteGuest(john);

        assertThat(manager.findGuestById(john.getId())).isNull();
        assertThat(manager.findGuestById(jane.getId())).isNotNull();
    }


    @Test
    public void deleteNullGuest() throws InvalidEntityException{
        expectedException.expect(IllegalArgumentException.class);
        manager.deleteGuest(null);
    }


    @Test
    public void deleteGuestWithNullId() throws InvalidEntityException{
        Guest john = johnBuilder().build();
        john.setId(null);
        expectedException.expect(InvalidEntityException.class);
        manager.deleteGuest(john);
    }

}