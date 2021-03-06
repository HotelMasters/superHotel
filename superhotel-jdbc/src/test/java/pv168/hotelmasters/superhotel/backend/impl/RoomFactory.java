package pv168.hotelmasters.superhotel.backend.impl;

import pv168.hotelmasters.superhotel.backend.entities.Room;

/**
 * @author Kristian Lesko
 */
public class RoomFactory {
    private Long id;
    private String name;
    private int capacity;
    private double price;

    public RoomFactory id(Long id) {
        this.id = id;
        return this;
    }

    public RoomFactory name(String name) {
        this.name = name;
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
        room.setName(name);
        room.setCapacity(capacity);
        room.setPrice(price);
        return room;
    }
}
