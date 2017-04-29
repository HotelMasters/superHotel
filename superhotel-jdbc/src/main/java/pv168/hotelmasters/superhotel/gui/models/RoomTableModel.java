package pv168.hotelmasters.superhotel.gui.models;

import pv168.hotelmasters.superhotel.backend.entities.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public class RoomTableModel extends UserInterfaceTableModel {
    private List<Room> rooms = new ArrayList<>();

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return getResourceBundle().getString("TEXT_NAME_ROOM");
            case 1:
                return getResourceBundle().getString("TEXT_CAPACITY_ROOM");
            case 2:
                return getResourceBundle().getString("TEXT_PRICE_ROOM");
            default:
                throw new IllegalArgumentException("No such column");
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return Double.class;
            default:
                throw new IllegalArgumentException("No such column");
        }
    }

    @Override
    public int getRowCount() {
        return rooms.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Room room = rooms.get(row);
        switch (column) {
            case 0:
                return room.getName();
            case 1:
                return room.getCapacity();
            case 2:
                return room.getPrice();
            default:
                throw new IllegalArgumentException("There is no column " + column);
        }
    }

    public void addRoom(Room room) {
        rooms.add(room);
        fireTableDataChanged();
    }

    public void deleteRoom(Room room) {
        rooms.remove(room);
        fireTableDataChanged();
    }

    public Room getRoom(int row) {
        return rooms.get(row);
    }
}
