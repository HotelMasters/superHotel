package pv168.hotelmasters.superhotel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.derby.jdbc.EmbeddedDataSource;
import pv168.hotelmasters.superhotel.db.Utilities;
import pv168.hotelmasters.superhotel.exceptions.DBException;
import pv168.hotelmasters.superhotel.exceptions.ValidationError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Kristian Lesko
 */
public class RoomManagerImplTest {
    private RoomManagerImpl manager;
    private DataSource dataSource;

    private void prepareDataSource() throws SQLException {
        EmbeddedDataSource embeddedDataSource = new EmbeddedDataSource();
        embeddedDataSource.setDatabaseName("memory:superhotel-test");
        embeddedDataSource.setCreateDatabase("create");
        dataSource = embeddedDataSource;
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws SQLException {
        prepareDataSource();
        Utilities.executeSql(getClass().getResource("createTables.sql"), dataSource);
        manager = new RoomManagerImpl();
        manager.setDataSource(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        Utilities.executeSql(getClass().getResource("dropTables.sql"), dataSource);
    }

    @Test
    public void createRoom() {
        Room room = createSampleEconomyRoom().build();
        manager.createRoom(room);

        Long roomId = room.getId();
        assertThat(roomId).isNotNull();
        Room createdRoom = manager.findRoomById(roomId);
        assertThat(createdRoom).isEqualTo(room).isNotSameAs(room);
        assertDeepEquals(room, createdRoom);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullRoom() {
        manager.createRoom(null);
    }

    @Test
    public void createRoomWithNegativeCapacity() {
        Room room = createSampleEconomyRoom().capacity(-2).build();
        exception.expect(ValidationError.class);
        manager.createRoom(room);
    }

    @Test
    public void createRoomWithNegativePrice() {
        Room room = createSampleDeluxeRoom().price(-120.3).build();
        exception.expect(ValidationError.class);
        manager.createRoom(room);
    }

    @Test
    public void updateRoom() {
        Room room = createSampleEconomyRoom().build();
        Room secondRoom = createSampleDeluxeRoom().build();
        manager.createRoom(room);
        manager.createRoom(secondRoom);
        Long roomId = room.getId();

        room = manager.findRoomById(roomId);
        room.setCapacity(4);
        manager.updateRoom(room);
        assertThat(room.getCapacity()).isEqualTo(4);

        room = manager.findRoomById(roomId);
        assertThat(room.getCapacity()).isEqualTo(4);
        room.setPrice(10.4);
        manager.updateRoom(room);
        assertThat(room.getPrice()).isEqualTo(Double.valueOf(10.4));

        room = manager.findRoomById(roomId);
        assertThat(room.getPrice()).isEqualTo(Double.valueOf(10.4));

        // Ensure update isolation
        assertDeepEquals(secondRoom, manager.findRoomById(secondRoom.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullRoom() {
        manager.updateRoom(null);
    }

    @Test
    public void updateRoomWithNullId() {
        Room room = createSampleEconomyRoom().build();
        manager.createRoom(room);

        room.setId(null);
        exception.expect(IllegalArgumentException.class);
        manager.updateRoom(room);
    }

    @Test
    public void updateRoomWithInvalidId() {
        Room room = createSampleEconomyRoom().build();
        manager.createRoom(room);

        room.setId(-42L);
        exception.expect(DBException.class);
        manager.updateRoom(room);
    }

    @Test
    public void updateRoomWithNegativeCapacity() {
        Room room = createSampleEconomyRoom().build();
        manager.createRoom(room);

        room.setCapacity(-1);
        exception.expect(ValidationError.class);
        manager.updateRoom(room);
    }

    @Test
    public void updateRoomWithNegativePrice() {
        Room room = createSampleEconomyRoom().build();
        manager.createRoom(room);

        room.setPrice(-420.2);
        exception.expect(ValidationError.class);
        manager.updateRoom(room);
    }

    @Test
    public void deleteRoom() {
        Room deletedRoom = createSampleEconomyRoom().build();
        manager.createRoom(deletedRoom);
        Long deletedRoomId = deletedRoom.getId();
        assertThat(deletedRoom).isNotNull();

        Room keptRoom = createSampleDeluxeRoom().build();
        manager.createRoom(keptRoom);
        assertThat(keptRoom).isNotNull();

        manager.deleteRoom(deletedRoom);
        deletedRoom = manager.findRoomById(deletedRoomId);

        assertThat(deletedRoom).isNull();
        assertThat(keptRoom).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullRoom() {
        manager.deleteRoom(null);
    }

    @Test
    public void findRoomByCapacity() {
        Room room31 = createSampleEconomyRoom().price(120).build();
        manager.createRoom(room31);
        Room room32 = createSampleEconomyRoom().price(140).build();
        manager.createRoom(room32);
        Room room21 = createSampleDeluxeRoom().price(890).build();
        manager.createRoom(room21);

        List<Room> expectedRooms3 = new ArrayList<>();
        expectedRooms3.add(room31);
        expectedRooms3.add(room32);
        List<Room> rooms3 = manager.findRoomByCapacity(3);

        assertDeepEquals(expectedRooms3, rooms3);
    }

    @Test
    public void findAllRooms() {
        Room room1 = createSampleEconomyRoom().capacity(3).build();
        manager.createRoom(room1);
        Room room2 = createSampleDeluxeRoom().capacity(1).build();
        manager.createRoom(room2);
        Room room3 = createSampleDeluxeRoom().capacity(2).build();
        manager.createRoom(room3);

        List<Room> expectedRooms = new ArrayList<>();
        expectedRooms.add(room1);
        expectedRooms.add(room2);
        expectedRooms.add(room3);
        List<Room> rooms = manager.findAllRooms();

        assertDeepEquals(expectedRooms, rooms);
    }

    private static RoomFactory createSampleEconomyRoom() {
        return new RoomFactory().capacity(3).price(500.7);
    }

    private static RoomFactory createSampleDeluxeRoom() {
        return new RoomFactory().capacity(2).price(1200.4);
    }

    private static void assertDeepEquals(Room expectedRoom, Room actualRoom) {
        assertThat(actualRoom.getId()).isEqualTo(expectedRoom.getId());
        assertThat(actualRoom.getCapacity()).isEqualTo(expectedRoom.getCapacity());
        assertThat(actualRoom.getPrice()).isEqualTo(expectedRoom.getPrice());
    }

    private static void assertDeepEquals(List<Room> expectedRooms, List<Room> actualRooms) {
        assertThat(actualRooms.size()).isEqualTo(expectedRooms.size());
        for (int i = 0; i < expectedRooms.size(); i++) {
            assertDeepEquals(expectedRooms.get(i), actualRooms.get(i));
        }
    }
}
