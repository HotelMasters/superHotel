package pv168.hotelmasters.superhotel;

/**
 * @author Kristian Lesko
 */
public class RoomFactory {
    private Long id;
    private int capacity;
    private double price;

    public RoomFactory id(Long id) {
        this.id = id;
        return this;
    }

    public RoomFactory capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public RoomFactory price(double price) {
        this.price = price;
        return this;
    }

    public Room build() {
        Room room = new Room();
        room.setId(id);
        room.setCapacity(capacity);
        room.setPrice(price);
        return room;
    }
}
