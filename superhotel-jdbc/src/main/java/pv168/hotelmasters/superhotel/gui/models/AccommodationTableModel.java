package pv168.hotelmasters.superhotel.gui.models;

import pv168.hotelmasters.superhotel.backend.entities.Accommodation;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public class AccommodationTableModel extends UserInterfaceTableModel {
    private List<Accommodation> accommodations = new ArrayList<>();

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return getResourceBundle().getString("TEXT_GUEST_ACCOM");
            case 1:
                return getResourceBundle().getString("TEXT_ROOM_ACCOM");
            case 2:
                return getResourceBundle().getString("TEXT_DATE_FROM_ACC");
            case 3:
                return getResourceBundle().getString("TEXT_DATE_TO_ACC");
            case 4:
                return getResourceBundle().getString("TEXT_PRICE_ROOM");
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
                case 3:
                    return String.class;
                case 4:
                    return Double.class;
                default:
                    throw new IllegalArgumentException("No such column");
            }
    }

    @Override
    public int getRowCount() {
        return accommodations.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Accommodation accommodation = accommodations.get(row);
        switch (column) {
            case 0:
                return accommodation.getGuest().getName();
            case 1:
                return accommodation.getRoom().getName();
            case 2:
                return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(accommodation.getDateFrom());
            case 3:
                return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(accommodation.getDateTo());
            case 4:
                return accommodation.getTotalPrice();
            default:
                throw new IllegalArgumentException("There is no column " + column);
        }
    }

    public void addAccommodation(Accommodation accommodation) {
        accommodations.add(accommodation);
        fireTableDataChanged();
    }

    public void deleteAccommodation(Accommodation accommodation) {
        accommodations.remove(accommodation);
        fireTableDataChanged();
    }

    public Accommodation getAccommodation(int row) {
        return accommodations.get(row);
    }
}
