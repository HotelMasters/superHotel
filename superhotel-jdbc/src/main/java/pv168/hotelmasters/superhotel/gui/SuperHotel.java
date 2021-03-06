package pv168.hotelmasters.superhotel.gui;

import com.toedter.calendar.JDateChooser;
import pv168.hotelmasters.superhotel.backend.db.DBCreator;
import pv168.hotelmasters.superhotel.backend.entities.Accommodation;
import pv168.hotelmasters.superhotel.backend.entities.Guest;
import pv168.hotelmasters.superhotel.backend.entities.Room;
import pv168.hotelmasters.superhotel.gui.models.AccommodationTableModel;
import pv168.hotelmasters.superhotel.gui.models.GuestTableModel;
import pv168.hotelmasters.superhotel.gui.models.RoomTableModel;
import pv168.hotelmasters.superhotel.backend.impl.AccommodationManagerImpl;
import pv168.hotelmasters.superhotel.backend.impl.GuestManagerImpl;
import pv168.hotelmasters.superhotel.backend.impl.RoomManagerImpl;
import pv168.hotelmasters.superhotel.backend.interfaces.AccommodationManager;
import pv168.hotelmasters.superhotel.backend.interfaces.GuestManager;
import pv168.hotelmasters.superhotel.backend.interfaces.RoomManager;
import pv168.hotelmasters.superhotel.gui.models.UserInterfaceTableModel;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public class SuperHotel {
    private DataSource dataSource;
    private static final Logger logger = Logger.getLogger("SuperHotel");
    private static LocalDate NOW = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();

    private JPanel mainPanel;

    private GuestManager guestManager;
    private GuestTableModel guestTableModel;
    private Guest editedGuest;
    private JTable guestTable;
    private JTextField guestAddName;
    private JTextPane guestAddNameHelp;
    private JTextField guestAddAddress;
    private JTextPane guestAddAddressHelp;
    private JDateChooser guestAddBirthday;
    private JTextPane guestAddBirthdayHelp;
    private JTextField guestAddCard;
    private JTextPane guestAddCardHelp;
    private JButton guestAdd;
    private JButton guestUpdate;
    private JButton guestDelete;
    private JTextArea guestInfoText;

    private RoomManager roomManager;
    private RoomTableModel roomTableModel;
    private Room editedRoom;
    private JTable roomTable;
    private JTextField roomAddName;
    private JTextPane roomAddNameHelp;
    private JTextField roomAddCapacity;
    private JTextPane roomAddCapacityHelp;
    private JTextField roomAddPrice;
    private JTextPane roomAddPriceHelp;
    private JButton roomAdd;
    private JButton roomUpdate;
    private JButton roomDelete;
    private JTextArea roomInfoText;

    private AccommodationManager accommodationManager;
    private AccommodationTableModel accommodationTableModel;
    private Accommodation editedAccommodation;
    private JTable accommodationTable;
    private JComboBox<Guest> accommodationAddGuest;
    private JTextArea accommodationAddGuestHelp;
    private JComboBox<Room> accommodationAddRoom;
    private JTextArea accommodationAddRoomHelp;
    private JDateChooser accommodationAddFrom;
    private JTextArea accommodationAddFromHelp;
    private JDateChooser accommodationAddTo;
    private JTextArea accommodationAddToHelp;
    private JButton accommodationAdd;
    private JButton accommodationUpdate;
    private JButton accommodationDelete;
    private JTextArea accommodationInfoText;

    private Color textColor = Color.WHITE;
    private Color errTextColor = Color.RED;

    public static void main(String[] args) {
        setLookAndFeel();
        SuperHotel app = new SuperHotel();
        app.initialize();
    }

    private void initialize() {
        InitSwingWorker worker = new InitSwingWorker();
        worker.execute();
        initGui();
        initDb(worker);
        initManagers();
        initTableModels();
    }

    private void initGui() {
        JFrame frame = new JFrame("SuperHotel");
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loadComponents();
        frame.pack();
        frame.setVisible(true);
    }

    private class InitSwingWorker extends SwingWorker<DataSource, Void> {
        @Override
        protected DataSource doInBackground() {
            return new DBCreator().createBasicDB();
        }

    }

    private void initDb(InitSwingWorker worker) {
        try {
            dataSource = worker.get();
        } catch (Exception e) {
            guestInfoText.setText("Error setting up database.");
        }
    }

    private void initManagers() {
        GuestManagerImpl guestManager = new GuestManagerImpl(Clock.systemDefaultZone());
        guestManager.setDataSource(dataSource);
        this.guestManager = guestManager;

        RoomManagerImpl roomManager = new RoomManagerImpl();
        roomManager.setDataSource(dataSource);
        this.roomManager = roomManager;

        AccommodationManagerImpl accommodationManager = new AccommodationManagerImpl(Clock.systemDefaultZone());
        accommodationManager.setDataSource(dataSource);
        this.accommodationManager = accommodationManager;

        clearAccommodationInputs();
    }

    private void initTableModels() {
        guestTableModel = new GuestTableModel(guestManager);
        guestTable.setModel(guestTableModel);
        roomTableModel = new RoomTableModel(roomManager);
        roomTable.setModel(roomTableModel);
        accommodationTableModel = new AccommodationTableModel(accommodationManager);
        accommodationTable.setModel(accommodationTableModel);
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warning("Error setting system Look and Feel, leaving default settings enabled.");
        }
    }

    private void loadComponents() {
        loadGuestPanel();
        loadRoomPanel();
        loadAccommodationPanel();
    }

    private void loadGuestPanel() {
        guestAddBirthday.setDate(Date.from(Instant.now()));
        guestAdd.addActionListener(this::handleGuestAdd);
        guestDelete.addActionListener(this::handleGuestDelete);
        guestUpdate.addActionListener(this::handleGuestUpdateInit);
    }

    private class GuestAddWorker extends SwingWorker<Void, Void> {
        private Guest guest;

        GuestAddWorker(Guest guest) {
            this.guest = guest;
        }

        @Override
        protected Void doInBackground() throws Exception {
            guestTableModel.addGuest(guest);
            return null;
        }

        @Override
        protected void done() {
            guestInfoText.setText(
                    localizedString("GUEST") + " " +
                            guest.getName() + " " + localizedString("GUEST_CREATE_SUCCESS"));
            clearGuestControls();
        }
    }

    private void handleGuestAdd(ActionEvent e) {
        Guest guest = parseGuest();
        if (guest != null) {
            new GuestAddWorker(guest).execute();
        }
    }

    private class GuestDeleteWorker extends SwingWorker<Void, Void> {
        private Guest guest;

        GuestDeleteWorker(Guest guest) {
            this.guest = guest;
        }

        @Override
        protected Void doInBackground() throws Exception {
            guestTableModel.deleteGuest(guest);
            return null;
        }

        @Override
        protected void done() {
            guestInfoText.setText(
                    localizedString("GUEST") + " " +
                            guest.getName() + " " + localizedString("GUEST_DELETE_SUCCESS"));
            clearGuestControls();
            clearAccommodationInputs();
        }
    }

    private void handleGuestDelete(ActionEvent e) {
        int deletedRow = guestTable.convertRowIndexToModel(guestTable.getSelectedRow());
        if (deletedRow >= 0) {
            Guest deletedGuest = guestTableModel.getGuest(deletedRow);
            if (checkGuestConstraint(deletedGuest)) {
                guestInfoText.setText(localizedString("GUEST") + " " + localizedString("GUEST_ACCOM_ERROR"));
                return;
            }
            new GuestDeleteWorker(deletedGuest).execute();
        }
    }

    private boolean checkGuestConstraint(Guest guest) {
        for (Accommodation accommodation : accommodationManager.findAllAccommodations()) {
            if (accommodation.getGuest().equals(guest)) {
                return true;
            }
        }
        return false;
    }

    private void handleGuestUpdateInit(ActionEvent e) {
        int row = guestTable.getSelectedRow();
        if (row == -1) {
            logger.fine("No row selected, not proceeding with guest update.");
            return;
        }
        Guest guest = guestTableModel.getGuest(guestTable.convertRowIndexToModel(row));
        setGuestInputs(guest);
        editedGuest = guest;
        removeActionListeners(guestAdd);
        guestAdd.addActionListener(this::handleGuestUpdateFinish);
        guestAdd.setText(localizedString("BUTTON_UPDATE_FINISH"));
    }

    private class GuestUpdateWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
            guestTableModel.updateGuest(editedGuest);
            return null;
        }

        @Override
        protected void done() {
            guestInfoText.setText(
                    localizedString("GUEST") + " " +
                            editedGuest.getName() + " " + localizedString("GUEST_UPDATE_SUCCESS"));
            clearGuestControls();
            editedGuest = null;
            guestAdd.setText(localizedString("BUTTON_ADD"));
        }
    }

    private void handleGuestUpdateFinish(ActionEvent e) {
        Guest parsed = parseGuest();
        if (parsed != null) {
            editedGuest.setName(parsed.getName());
            editedGuest.setAddress(parsed.getAddress());
            editedGuest.setBirthDay(parsed.getBirthDay());
            editedGuest.setCrCardNumber(parsed.getCrCardNumber());
            new GuestUpdateWorker().execute();
            removeActionListeners(guestAdd);
            guestAdd.addActionListener(this::handleGuestAdd);
            clearAccommodationInputs();
        }
    }

    private Guest parseGuest() {
        clearGuestColors();
        String errs = "";
        Guest guest = new Guest();

        guest.setName(guestAddName.getText());
        if (guest.getName().length() == 0) {
            errs += localizedString("ERR_NAME_EMPTY")+ "\n";
            guestAddNameHelp.setForeground(errTextColor);
        }
        guest.setAddress(guestAddAddress.getText());
        if (guest.getAddress().length() == 0) {
            errs += localizedString("ERR_ADDRESS_EMPTY")+ "\n";
            guestAddAddressHelp.setForeground(errTextColor);
        }
        guest.setBirthDay(getLocalDate(guestAddBirthday.getDate()));
        if (!guest.getBirthDay().isBefore(NOW)) {
            errs += localizedString("ERR_BIRTHDAY_FUTURE")+ "\n";
            guestAddBirthdayHelp.setForeground(errTextColor);
        }
        try {
            guest.setCrCardNumber(Long.parseLong(guestAddCard.getText()));
        } catch (NumberFormatException ex) {
            if (guestAddCard.getText().length() == 0) {
                errs += localizedString("ERR_CARD_EMPTY");
            } else {
                errs += localizedString("ERR_CARD_NUMBER");
            }
            guestAddCardHelp.setForeground(errTextColor);
        }
        guestInfoText.setText(errs);
        if (errs.length() > 0) {
            return null;
        }
        return guest;
    }

    private void setGuestInputs(Guest guest) {
        guestAddName.setText(guest.getName());
        guestAddAddress.setText(guest.getAddress());
        guestAddBirthday.setDate(getDate(guest.getBirthDay()));
        guestAddCard.setText(guest.getCrCardNumber().toString());
    }

    private void clearGuestControls() {
        clearGuestColors();
        clearGuestInputs();
    }

    private void clearGuestColors() {
        guestAddNameHelp.setForeground(textColor);
        guestAddAddressHelp.setForeground(textColor);
        guestAddBirthdayHelp.setForeground(textColor);
        guestAddCardHelp.setForeground(textColor);
    }

    private void clearGuestInputs() {
        guestAddName.setText("");
        guestAddAddress.setText("");
        guestAddBirthday.setDate(Date.from(Instant.now()));
        guestAddCard.setText("");
    }

    private void loadRoomPanel() {
        roomAdd.addActionListener(this::handleRoomAdd);
        roomUpdate.addActionListener(this::handleRoomUpdateInit);
        roomDelete.addActionListener(this::handleRoomDelete);
    }

    private class RoomAddWorker extends SwingWorker<Void, Void> {
        private Room room;

        RoomAddWorker(Room room) {
            this.room = room;
        }

        @Override
        protected Void doInBackground() throws Exception {
            roomTableModel.addRoom(room);
            return null;
        }

        @Override
        protected void done() {
            roomInfoText.setText(
                    localizedString("ROOM") + " " +
                            room.getName() + " " + localizedString("ROOM_CREATE_SUCCESS"));
            clearRoomControls();
            clearAccommodationInputs();
        }
    }

    private void handleRoomAdd(ActionEvent e) {
        Room room = parseRoom();
        if (room != null) {
            new RoomAddWorker(room).execute();
        }
    }

    private class RoomDeleteWorker extends SwingWorker<Void, Void> {
        Room room;

        RoomDeleteWorker(Room room) {
            this.room = room;
        }

        @Override
        protected Void doInBackground() throws Exception {
            roomTableModel.deleteRoom(room);
            return null;
        }

        @Override
        protected void done() {
            roomInfoText.setText(
                    localizedString("ROOM") + " " +
                            room.getName() + " " + localizedString("ROOM_DELETE_SUCCESS"));
            clearAccommodationInputs();
        }
    }

    private void handleRoomDelete(ActionEvent e) {
        int deletedRow = roomTable.convertRowIndexToModel(roomTable.getSelectedRow());
        if (deletedRow >= 0) {
            Room deletedRoom = roomTableModel.getRoom(deletedRow);
            if (checkRoomConstraint(deletedRoom)) {
                roomInfoText.setText(localizedString("ROOM") + " " + localizedString("ROOM_OCCUPIED"));
                return;
            }
            new RoomDeleteWorker(deletedRoom).execute();
        }
    }

    private boolean checkRoomConstraint(Room room) {
        for (Accommodation accommodation : accommodationManager.findAllAccommodations()) {
            if (accommodation.getRoom().equals(room)) {
                return true;
            }
        }
        return false;
    }

    private void handleRoomUpdateInit(ActionEvent e) {
        int row = roomTable.getSelectedRow();
        if (row == -1) {
            logger.fine("No row selected, not proceeding with room update.");
            return;
        }
        Room room = roomTableModel.getRoom(roomTable.convertRowIndexToModel(row));
        setRoomInputs(room);
        editedRoom = room;
        removeActionListeners(roomAdd);
        roomAdd.addActionListener(this::handleRoomUpdateFinish);
        roomAdd.setText(localizedString("BUTTON_UPDATE_FINISH"));
    }

    private class RoomUpdateWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
            roomTableModel.updateRoom(editedRoom);
            return null;
        }

        @Override
        protected void done() {
            roomInfoText.setText(
                    localizedString("ROOM") + " " +
                            editedRoom.getName() + " " + localizedString("ROOM_UPDATE_SUCCESS"));
            clearRoomControls();
            editedRoom = null;
            roomAdd.setText(localizedString("BUTTON_ADD"));
        }
    }

    private void handleRoomUpdateFinish(ActionEvent e) {
        Room parsed = parseRoom();
        if (parsed != null) {
            editedRoom.setName(parsed.getName());
            editedRoom.setCapacity(parsed.getCapacity());
            editedRoom.setPrice(parsed.getPrice());
            new RoomUpdateWorker().execute();
            removeActionListeners(roomAdd);
            roomAdd.addActionListener(this::handleRoomAdd);
            clearAccommodationInputs();
        }
    }

    private Room parseRoom() {
        clearRoomColors();
        String errs = "";
        Room room = new Room();

        room.setName(roomAddName.getText());
        if (room.getName().length() == 0) {
            errs += localizedString("ERR_NAME_EMPTY") + "\n";
            roomAddNameHelp.setForeground(errTextColor);
        }
        try {
            room.setCapacity(Integer.parseInt(roomAddCapacity.getText()));
        } catch (NumberFormatException ex) {
            if (roomAddCapacity.getText().length() == 0) {
                errs += localizedString("ERR_CAPACITY_EMPTY") + "\n";
            } else {
                errs += localizedString("ERR_CAPACITY_NUMBER") + "\n";
            }
            roomAddCapacityHelp.setForeground(errTextColor);
        }
        try {
            room.setPrice(Double.parseDouble(roomAddPrice.getText()));
        } catch (NumberFormatException ex) {
            if (roomAddCapacity.getText().length() == 0) {
                errs += localizedString("ERR_PRICE_EMPTY") + "\n";
            } else {
                errs += localizedString("ERR_PRICE_NUMBER") + "\n";
            }
            roomAddPriceHelp.setForeground(errTextColor);
        }
        roomInfoText.setText(errs);
        if (errs.length() > 0) {
            return null;
        }
        return room;
    }

    private void setRoomInputs(Room room) {
        roomAddName.setText(room.getName());
        roomAddCapacity.setText(Integer.valueOf(room.getCapacity()).toString());
        roomAddPrice.setText(room.getPrice().toString());
    }

    private void clearRoomControls() {
        clearRoomColors();
        clearRoomInputs();
    }

    private void clearRoomColors() {
        roomAddNameHelp.setForeground(textColor);
        roomAddCapacityHelp.setForeground(textColor);
        roomAddPriceHelp.setForeground(textColor);
    }

    private void clearRoomInputs() {
        roomAddName.setText("");
        roomAddCapacity.setText("");
        roomAddPrice.setText("");
    }

    private void loadAccommodationPanel() {
        accommodationAdd.addActionListener(this::handleAccommodationAdd);
        accommodationUpdate.addActionListener(this::handleAccommodationUpdateInit);
        accommodationDelete.addActionListener(this::handleAccommodationDelete);
    }

    private class AccommodationAddWorker extends SwingWorker<Void, Void> {
        Accommodation accommodation;

        AccommodationAddWorker(Accommodation accommodation) {
            this.accommodation = accommodation;
        }

        @Override
        protected Void doInBackground() {
            accommodationTableModel.addAccommodation(accommodation);
            return null;
        }

        @Override
        protected void done() {
            roomInfoText.setText(localizedString("ACCOMMODATION_CREATE_SUCCESS"));
            clearAccommodationControls();
        }
    }

    private void handleAccommodationAdd(ActionEvent e) {
        Accommodation accommodation = parseAccommodation();
        if (accommodation != null) {
            calculateAccommodationPrice(accommodation);
            new AccommodationAddWorker(accommodation).execute();
        }
    }

    private class AccommodationDeleteWorker extends SwingWorker<Void, Void> {
        Accommodation accommodation;

        AccommodationDeleteWorker(int row) {
            this.accommodation = accommodationTableModel.getAccommodation(row);
        }

        @Override
        protected Void doInBackground() {
            accommodationTableModel.deleteAccommodation(accommodation);
            return null;
        }

        @Override
        protected void done() {
            roomInfoText.setText(localizedString("ACCOMMODATION_DELETE_SUCCESS"));
        }
    }

    private void handleAccommodationDelete(ActionEvent e) {
        int deletedRow = accommodationTable.convertRowIndexToModel(accommodationTable.getSelectedRow());
        if (deletedRow >= 0) {
            new AccommodationDeleteWorker(deletedRow).execute();
        }
    }

    private void handleAccommodationUpdateInit(ActionEvent e) {
        int row = accommodationTable.getSelectedRow();
        if (row == -1) {
            logger.fine("No row selected, not proceeding with accommodation update.");
        }
        Accommodation accommodation = accommodationTableModel.getAccommodation(
                accommodationTable.convertRowIndexToModel(row));
        setAccommodationInputs(accommodation);
        editedAccommodation = accommodation;
        removeActionListeners(accommodationAdd);
        accommodationAdd.addActionListener(this::handleAccommodationUpdateFinish);
        accommodationAdd.setText(localizedString("BUTTON_UPDATE_FINISH"));
    }

    private class AccommodationUpdateWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() {
            accommodationTableModel.updateAccommodation(editedAccommodation);
            return null;
        }

        @Override
        protected void done() {
            roomInfoText.setText(localizedString("ACCOMMODATION_UPDATE_SUCCESS"));
            clearAccommodationControls();
            editedAccommodation = null;
            accommodationAdd.setText(localizedString("BUTTON_MAKE"));
        }
    }

    private void handleAccommodationUpdateFinish(ActionEvent e) {
        Accommodation parsed = parseAccommodation();
        if (parsed != null) {
            editedAccommodation.setGuest(parsed.getGuest());
            editedAccommodation.setRoom(parsed.getRoom());
            editedAccommodation.setDateFrom(parsed.getDateFrom());
            editedAccommodation.setDateTo(parsed.getDateTo());
            calculateAccommodationPrice(editedAccommodation);
            new AccommodationUpdateWorker().execute();
            removeActionListeners(accommodationAdd);
            accommodationAdd.addActionListener(this::handleAccommodationAdd);
        }
    }

    private Accommodation parseAccommodation() {
        clearAccommodationColors();
        String errs = "";
        Accommodation accommodation = new Accommodation();

        accommodation.setGuest((Guest) accommodationAddGuest.getSelectedItem());
        accommodation.setRoom((Room) accommodationAddRoom.getSelectedItem());
        accommodation.setDateFrom(getLocalDate(accommodationAddFrom.getDate()));
        accommodation.setDateTo(getLocalDate(accommodationAddTo.getDate()));
        if (!accommodation.getDateTo().isAfter(accommodation.getDateFrom())) {
            errs += localizedString("ERR_ACCOMMODATION_DATE");
        }
        accommodationInfoText.setText(errs);
        if (errs.length() > 0) {
            return null;
        }
        return accommodation;
    }

    private void calculateAccommodationPrice(Accommodation accommodation) {
        accommodation.setTotalPrice(
                ChronoUnit.DAYS.between(accommodation.getDateFrom(), accommodation.getDateTo()) *
                        accommodation.getRoom().getPrice());
    }

    private void setAccommodationInputs(Accommodation accommodation) {
        accommodationAddGuest.setSelectedItem(accommodation.getGuest());
        accommodationAddRoom.setSelectedItem(accommodation.getRoom());
        accommodationAddFrom.setDate(getDate(accommodation.getDateFrom()));
        accommodationAddTo.setDate(getDate(accommodation.getDateTo()));
    }

    private void clearAccommodationControls() {
        clearAccommodationColors();
        clearAccommodationInputs();
    }

    private void clearAccommodationColors() {
        accommodationAddGuestHelp.setForeground(textColor);
        accommodationAddRoomHelp.setForeground(textColor);
        accommodationAddFromHelp.setForeground(textColor);
        accommodationAddToHelp.setForeground(textColor);
    }

    private void clearAccommodationInputs() {
        refreshAccommodationGuestOptions();
        refreshAccommodationRoomOptions();
        refreshAccommodationDates();
    }

    private void refreshAccommodationGuestOptions() {
        accommodationAddGuest.removeAllItems();
        for (Guest guest : guestManager.findAllGuests()) {
            accommodationAddGuest.addItem(guest);
        }
    }

    private void refreshAccommodationRoomOptions() {
        accommodationAddRoom.removeAllItems();
        for (Room room : roomManager.findAllRooms()) {
            accommodationAddRoom.addItem(room);
        }
    }

    private void refreshAccommodationDates() {
        accommodationAddFrom.setDate(Date.from(Instant.now()));
        accommodationAddTo.setDate(Date.from(Instant.now()));
    }

    private void removeActionListeners(JButton button) {
        for (ActionListener listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }
    private LocalDate getLocalDate(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date getDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    private String localizedString(String key) {
        return UserInterfaceTableModel.getResourceBundle().getString(key);
    }
}
