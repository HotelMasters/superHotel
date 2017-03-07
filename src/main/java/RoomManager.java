import java.util.List;

/**
 * @author 445434
 */
public interface RoomManager {

    void createRoom(Room room);
    void updateRoom(Room room);
    void deleteRoom(Room room);
    Room findRoomById(Long roomId);
    List<Room> findRoomByCapacity(int capacity);
    List<Room> findAllRooms();

}
