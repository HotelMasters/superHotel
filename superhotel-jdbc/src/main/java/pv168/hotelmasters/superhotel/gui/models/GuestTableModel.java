package pv168.hotelmasters.superhotel.gui.models;

import pv168.hotelmasters.superhotel.backend.entities.Guest;
import pv168.hotelmasters.superhotel.backend.interfaces.GuestManager;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Logger;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public class GuestTableModel extends UserInterfaceTableModel {
    private GuestManager manager;
    private Logger logger = Logger.getLogger("GuestTableModel");

    public GuestTableModel(GuestManager manager) {
        this.manager = manager;
        logger.fine("Guest table model initialized with " + manager);
    }

    @Override
    public String getColumnName(int column) {
        logger.fine("Retrieving name of column no. " + column);
        switch (column) {
            case 0:
                return getResourceBundle().getString("TEXT_NAME_GUEST");
            case 1:
                return getResourceBundle().getString("TEXT_ADDRESS_GUEST");
            case 2:
                return getResourceBundle().getString("TEXT_BIRTHDAY_GUEST");
            case 3:
                return getResourceBundle().getString("TEXT_CRCARDNUM_GUEST");
            default:
                throw new IllegalArgumentException("No such column");
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        logger.fine("Retrieving column class for column no. " + column);
        switch (column) {
            case 0:
            case 1:
            case 2:
                return String.class;
            case 3:
                return Long.class;
            default:
                throw new IllegalArgumentException("No such column");
        }
    }

    @Override
    public int getRowCount() {
        logger.fine("Retrieving row count");
        return manager.findAllGuests().size();
    }

    @Override
    public int getColumnCount() {
        logger.fine("Retrieving column count");
        return 4;
    }

    @Override
    public Object getValueAt(int row, int column) {
        logger.fine("Retrieving value at row " + row + ", column " + column);
        Guest guest = manager.findAllGuests().get(row);
        switch (column) {
            case 0:
                return guest.getName();
            case 1:
                return guest.getAddress();
            case 2:
                return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(guest.getBirthDay());
            case 3:
                return guest.getCrCardNumber();
            default:
                throw new IllegalArgumentException("There is no column " + column);
        }
    }

    public void addGuest(Guest guest) {
        logger.fine("Creating guest " + guest);
        try {
            manager.createGuest(guest);
        } catch (SQLException e) {
            logger.severe("Error creating a guest: " + e);
        }
        fireTableDataChanged();
    }

    public void updateGuest(Guest guest) {
        logger.fine("Updating guest " + guest);
        manager.updateGuest(guest);
        fireTableDataChanged();
    }

    public void deleteGuest(Guest guest) {
        logger.fine("Deleting guest " + guest);
        manager.deleteGuest(guest);
        fireTableDataChanged();
    }

    public Guest getGuest(int row) {
        logger.fine("Retrieving guest at row " + row);
        return manager.findAllGuests().get(row);
    }
}
