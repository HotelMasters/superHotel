package pv168.hotelmasters.superhotel.gui.models;

import pv168.hotelmasters.superhotel.backend.entities.Accommodation;
import pv168.hotelmasters.superhotel.backend.interfaces.AccommodationManager;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Logger;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public class AccommodationTableModel extends UserInterfaceTableModel {
    private AccommodationManager manager;
    private Logger logger = Logger.getLogger("AccommodationTableModel");

    public AccommodationTableModel(AccommodationManager manager) {
        this.manager = manager;
        logger.fine("Accommodation table model initialized with " + manager);
    }

    @Override
    public String getColumnName(int column) {
        logger.fine("Retrieving name of column no. " + column);
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
            logger.fine("Retrieving column class for column no. " + column);
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
        logger.fine("Retrieving row count");
        return manager.findAllAccommodations().size();
    }

    @Override
    public int getColumnCount() {
        logger.fine("Retrieving column count");
        return 5;
    }

    @Override
    public Object getValueAt(int row, int column) {
        logger.fine("Retrieving value at row " + row + ", column " + column);
        Accommodation accommodation = manager.findAllAccommodations().get(row);
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
        logger.fine("Creating accommodation " + accommodation);
        manager.createAccommodation(accommodation);
        fireTableDataChanged();
    }

    public void updateAccommodation(Accommodation accommodation) {
        logger.fine("Updating accommodation " + accommodation);
        manager.updateAccommodation(accommodation);
        fireTableDataChanged();
    }

    public void deleteAccommodation(Accommodation accommodation) {
        logger.fine("Deleting accommodation " + accommodation);
        manager.deleteAccommodation(accommodation);
        fireTableDataChanged();
    }

    public Accommodation getAccommodation(int row) {
        logger.fine("Retrieving accommodation at row " + row);
        return manager.findAllAccommodations().get(row);
    }
}
