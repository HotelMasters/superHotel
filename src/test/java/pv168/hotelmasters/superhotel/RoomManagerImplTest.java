package pv168.hotelmasters.superhotel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Kristian Lesko
 */
public class RoomManagerImplTest {
    private RoomManagerImpl manager;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        manager = new RoomManagerImpl();
    }

    @Test
    public void createRoom() {
        Room room = newRoom(4, 1500.2);
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
        Room room = newRoom(-2, 1000);
        exception.expect(IllegalArgumentException.class);
        manager.createRoom(room);
    }

    @Test
    public void createRoomWithNegativePrice() {
        Room room = newRoom(7, -200.5);
        exception.expect(IllegalArgumentException.class);
        manager.createRoom(room);
    }

    @Test
    public void updateRoom() {
        Room room = newRoom(7, 100.3);
        Room secondRoom = newRoom(5, 200.0);
        manager.createRoom(room);
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
        Room room = newRoom(7, 100.2);
        manager.createRoom(room);

        room.setId(null);
        exception.expect(IllegalArgumentException.class);
        manager.updateRoom(room);
    }

    @Test
    public void updateRoomWithInvalidId() {
        Room room = newRoom(7, 100.2);
        manager.createRoom(room);

        room.setId(room.getId() + 1);
        exception.expect(IllegalArgumentException.class);
        manager.updateRoom(room);
    }

    @Test
    public void updateRoomWithNegativeCapacity() {
        Room room = newRoom(7, 100.1);
        manager.createRoom(room);

        room.setCapacity(-1);
        exception.expect(IllegalArgumentException.class);
        manager.updateRoom(room);
    }

    @Test
    public void updateRoomWithNegativePrice() {
        Room room = newRoom(7, 100.0);
        manager.createRoom(room);

        room.setPrice(-420.2);
        exception.expect(IllegalArgumentException.class);
        manager.updateRoom(room);
    }

    @Test
    public void deleteRoom() {
        Room deletedRoom = newRoom(5, 42.8);
        manager.createRoom(deletedRoom);
        assertThat(deletedRoom).isNotNull();

        Room keptRoom = newRoom(7, 1200.50);
        manager.createRoom(keptRoom);
        assertThat(keptRoom).isNotNull();

        manager.deleteRoom(deletedRoom);

        assertThat(deletedRoom).isNull();
        assertThat(keptRoom).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullRoom() {
        manager.deleteRoom(null);
    }

    @Test
    public void findRoomByCapacity() {
        Room room41 = newRoom(4, 1200.3);
        manager.createRoom(room41);
        Room room42 = newRoom(4, 1500.8);
        manager.createRoom(room42);
        Room room31 = newRoom(3, 1200.1);
        manager.createRoom(room31);

        List<Room> expectedRooms4 = new ArrayList<>();
        expectedRooms4.add(room41);
        expectedRooms4.add(room42);
        List<Room> rooms4 = manager.findRoomByCapacity(4);

        assertDeepEquals(expectedRooms4, rooms4);
    }

    @Test
    public void findAllRooms() throws Exception {
        Room room1 = newRoom(4, 1200.3);
        manager.createRoom(room1);
        Room room2 = newRoom(4, 1500.8);
        manager.createRoom(room2);
        Room room3 = newRoom(3, 1200.1);
        manager.createRoom(room3);

        List<Room> expectedRooms = new ArrayList<>();
        expectedRooms.add(room1);
        expectedRooms.add(room2);
        expectedRooms.add(room3);
        List<Room> rooms = manager.findAllRooms();

        assertDeepEquals(expectedRooms, rooms);
    }

    private static Room newRoom(int capacity, double price) {
        Room room = new Room();
        room.setCapacity(capacity);
        room.setPrice(price);
        return room;
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
