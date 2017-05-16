package pv168.hotelmasters.superhotel.gui.models;

import javax.swing.table.AbstractTableModel;
import java.util.ResourceBundle;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public abstract class UserInterfaceTableModel extends AbstractTableModel {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("SuperHotel");

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
