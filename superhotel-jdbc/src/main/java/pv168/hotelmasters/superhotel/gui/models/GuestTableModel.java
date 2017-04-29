package pv168.hotelmasters.superhotel.gui.models;

import pv168.hotelmasters.superhotel.backend.entities.Guest;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public class GuestTableModel extends UserInterfaceTableModel {
    private List<Guest> guests = new ArrayList<>();

    @Override
    public String getColumnName(int column) {
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
        return guests.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Guest guest = guests.get(row);
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
        guests.add(guest);
        fireTableDataChanged();
    }

    public void deleteGuest(Guest guest) {
        guests.remove(guest);
        fireTableDataChanged();
    }

    public Guest getGuest(int row) {
        return guests.get(row);
    }
}
