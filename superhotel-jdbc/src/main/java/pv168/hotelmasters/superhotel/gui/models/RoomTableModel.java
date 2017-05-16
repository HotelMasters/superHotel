package pv168.hotelmasters.superhotel.gui.models;

import pv168.hotelmasters.superhotel.backend.entities.Room;
import pv168.hotelmasters.superhotel.backend.interfaces.RoomManager;

import java.util.logging.Logger;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public class RoomTableModel extends UserInterfaceTableModel {
    private RoomManager manager;
    private Logger logger = Logger.getLogger("GuestTableModel");

    public RoomTableModel(RoomManager manager) {
        this.manager = manager;
        logger.fine("Room table model initialized with " + manager);
    }

    @Override
    public String getColumnName(int column) {
        logger.fine("Retrieving name of column no. " + column);
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
        logger.fine("Retrieving column class for column no. " + column);
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
        logger.fine("Retrieving row count");
        return manager.findAllRooms().size();
    }

    @Override
    public int getColumnCount() {
        logger.fine("Retrieving column count");
        return 3;
    }

    @Override
    public Object getValueAt(int row, int column) {
        logger.fine("Retrieving value at row " + row + ", column " + column);
        Room room = manager.findAllRooms().get(row);
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
        logger.fine("Creating room " + room);
        manager.createRoom(room);
        fireTableDataChanged();
    }

    public void updateRoom(Room room) {
        logger.fine("Updating room " + room);
        manager.updateRoom(room);
        fireTableDataChanged();
    }

    public void deleteRoom(Room room) {
        logger.fine("Deleting room " + room);
        manager.deleteRoom(room);
        fireTableDataChanged();
    }

    public Room getRoom(int row) {
        logger.fine("Retrieving room at row " + row);
        return manager.findAllRooms().get(row);
    }
}
