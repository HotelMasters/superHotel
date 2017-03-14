package pv168.hotelmasters.superhotel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kristian Lesko
 */
public class RoomManagerImplTest {
    private RoomManagerImpl manager;

    @Before
    public void setUp() {
        manager = new RoomManagerImpl();
    }

    @Test
    public void createRoom() {
        Room room = newRoom(4, 1500.2);
        manager.createRoom(room);

        Long roomId = room.getId();
        assertNotNull(roomId);
        Room createdRoom = manager.findRoomById(roomId);
        assertEquals("The room retrieved from the manager differs from the saved one", room, createdRoom);
        assertNotSame("The room retrieved from the manager is the same one that was saved", room, createdRoom);
        assertDeepEquals(room, createdRoom);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullRoom() {
        manager.createRoom(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createRoomWithNegativeCapacity() {
        Room room = newRoom(-2, 1000);
        manager.createRoom(room);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createRoomWithNegativePrice() {
        Room room = newRoom(7, -200.5);
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
        assertEquals(4, room.getCapacity());

        room = manager.findRoomById(roomId);
        assertEquals(4, room.getCapacity());
        room.setPrice(10.4);
        manager.updateRoom(room);
        assertEquals(Double.valueOf(10.4), room.getPrice());

        room = manager.findRoomById(roomId);
        assertEquals(Double.valueOf(10.4), room.getPrice());

        // Ensure update isolation
        assertDeepEquals(secondRoom, manager.findRoomById(secondRoom.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullRoom() {
        manager.updateRoom(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithNullId() {
        Room room = newRoom(7, 100.2);
        manager.createRoom(room);

        room.setId(null);
        manager.updateRoom(room);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithInvalidId() {
        Room room = newRoom(7, 100.2);
        manager.createRoom(room);

        room.setId(room.getId() + 1);
        manager.updateRoom(room);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithNegativeCapacity() {
        Room room = newRoom(7, 100.1);
        manager.createRoom(room);

        room.setCapacity(-1);
        manager.updateRoom(room);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRoomWithNegativePrice() {
        Room room = newRoom(7, 100.0);
        manager.createRoom(room);

        room.setPrice(-420.2);
        manager.updateRoom(room);
    }

    @Test
    public void deleteRoom() {
        Room deletedRoom = newRoom(5, 42.8);
        manager.createRoom(deletedRoom);
        assertNotNull(deletedRoom.getId());

        Room keptRoom = newRoom(7, 1200.50);
        manager.createRoom(keptRoom);
        assertNotNull(keptRoom.getId());

        manager.deleteRoom(deletedRoom);

        assertNull(deletedRoom.getId());
        assertNotNull(keptRoom.getId());
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
        assertEquals(expectedRoom.getId(), actualRoom.getId());
        assertEquals(expectedRoom.getCapacity(), actualRoom.getCapacity());
        assertEquals(expectedRoom.getPrice(), actualRoom.getPrice());
    }

    private static void assertDeepEquals(List<Room> expectedRooms, List<Room> actualRooms) {
        assertEquals(expectedRooms.size(), actualRooms.size());
        for (int i = 0; i < expectedRooms.size(); i++) {
            assertDeepEquals(expectedRooms.get(i), actualRooms.get(i));
        }
    }
}
