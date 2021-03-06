package pv168.hotelmasters.superhotel.backend.impl;

import pv168.hotelmasters.superhotel.backend.db.Utilities;
import pv168.hotelmasters.superhotel.backend.entities.Accommodation;
import pv168.hotelmasters.superhotel.backend.entities.Guest;
import pv168.hotelmasters.superhotel.backend.entities.Room;
import pv168.hotelmasters.superhotel.backend.exceptions.DBException;
import pv168.hotelmasters.superhotel.backend.exceptions.InvalidEntityException;
import pv168.hotelmasters.superhotel.backend.exceptions.ValidationError;
import pv168.hotelmasters.superhotel.backend.interfaces.AccommodationManager;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Gabriela Godiskova, Kristian Lesko
 */
public class AccommodationManagerImpl implements AccommodationManager {
    private static final Logger logger = Logger.getLogger("RoomManagerImpl");
    private DataSource dataSource;
    private Clock clock;
    private GuestManagerImpl guestManager;
    private RoomManagerImpl roomManager;

    public AccommodationManagerImpl(Clock clock) {
        this.clock = clock;
        guestManager = new GuestManagerImpl(clock);
        roomManager = new RoomManagerImpl();
        logger.fine("Initialized RoomManagerImpl object");
    }

    public void setDataSource(DataSource source) {
        dataSource = source;
        guestManager.setDataSource(source);
        roomManager.setDataSource(source);
    }

    private void checkDataSource() {
        logger.fine("Checking data source");
        if (dataSource == null) {
            throw new IllegalStateException("Data source must not be null");
        }
    }

    @Override
    public void createAccommodation(Accommodation accommodation) {
        checkDataSource();
        validateAccommodation(accommodation);
        if (accommodation.getId() != null) {
            throw new InvalidEntityException("Accommodation already in database");
        }
        String query = "INSERT INTO accommodation (guestId, roomId, startDate, endDate, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, accommodation.getGuest().getId());
            statement.setLong(2, accommodation.getRoom().getId());
            statement.setDate(3, Date.valueOf(accommodation.getDateFrom()));
            statement.setDate(4, Date.valueOf(accommodation.getDateTo()));
            statement.setDouble(5, accommodation.getTotalPrice());
            logger.fine("Executing accommodation create statement " + statement);
            int count = statement.executeUpdate();
            Utilities.checkUpdateSanity(count, true);

            accommodation.setId(Utilities.parseId(statement.getGeneratedKeys()));
            logger.info("Accommodation " + accommodation + " created");
        } catch (SQLException e) {
            logger.severe("Error creating accommodation: " + e);
            throw new DBException(e);
        }
    }

    @Override
    public void updateAccommodation(Accommodation accommodation) {
        checkDataSource();
        validateAccommodation(accommodation);
        if (accommodation.getId() == null) {
            throw new InvalidEntityException("Accommodation does not exist in the DB, cannot update");
        }
        String query = "UPDATE accommodation SET guestId = ?, roomId = ?," +
                "startDate = ?, endDate = ?, price = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, accommodation.getGuest().getId());
            statement.setLong(2, accommodation.getRoom().getId());
            statement.setDate(3, Date.valueOf(accommodation.getDateFrom()));
            statement.setDate(4, Date.valueOf(accommodation.getDateTo()));
            statement.setDouble(5, accommodation.getTotalPrice());
            statement.setLong(6, accommodation.getId());
            int count = statement.executeUpdate();
            Utilities.checkUpdateSanity(count, false);
            logger.info("Accommodation " + accommodation + " updated");
        } catch (SQLException e) {
            logger.severe("Error updating accommodation: " + e);
            throw new DBException(e);
        }
    }

    @Override
    public void deleteAccommodation(Accommodation accommodation) {
        checkDataSource();
        if (accommodation == null) {
            throw new IllegalArgumentException("Cannot delete null accommodation");
        }
        if (accommodation.getId() == null) {
            throw new InvalidEntityException("Accommodation does not exist in the DB, cannot delete");
        }
        String query = "DELETE FROM accommodation WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, accommodation.getId());
            int count = statement.executeUpdate();
            Utilities.checkUpdateSanity(count, true);
            logger.info("Accommodation " + accommodation + " deleted");
        } catch (SQLException e) {
            logger.severe("Error deleting accommodation: " + e);
            throw new DBException(e);
        }
    }

    @Override
    public Room findRoomByGuest(Guest guest) {
        String query = "SELECT id, guestId, roomId, startDate, endDate, price FROM accommodation WHERE guestId = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, guest.getId());
            List<Accommodation> accommodations = multipleItemQuery(statement);
            for (Accommodation accommodation : accommodations) {
                LocalDate now = LocalDate.now(clock);
                if (accommodation.getDateFrom().isBefore(now) && accommodation.getDateTo().isAfter(now)) {
                    logger.info("Found current room for guest " + guest);
                    return accommodation.getRoom();
                }
            }
            logger.info("No current room found for guest " + guest);
            return null;
        } catch (SQLException e) {
            logger.severe("Error finding accommodation by ID: " + e);
            throw new DBException(e);
        }
    }

    @Override
    public Guest findGuestByRoom(Room room) {
        String query = "SELECT id, guestId, roomId, startDate, endDate, price FROM accommodation WHERE roomId = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, room.getId());
            List<Accommodation> accommodations = multipleItemQuery(statement);
            for (Accommodation accommodation : accommodations) {
                LocalDate now = LocalDate.now(clock);
                logger.info("now = " + now);
                logger.info("from = " + accommodation.getDateFrom());
                logger.info("to = " + accommodation.getDateTo());
                if (accommodation.getDateFrom().isBefore(now) && accommodation.getDateTo().isAfter(now)) {
                    logger.info("Found current guest for room " + room);
                    return accommodation.getGuest();
                }
            }
            logger.info("No current guest found for room " + room);
            return null;
        } catch (SQLException e) {
            logger.severe("Error finding accommodation by ID: " + e);
            throw new DBException(e);
        }
    }

    @Override
    public Accommodation findAccommodationById(Long accommodationId) {
        String query = "SELECT id, guestId, roomId, startDate, endDate, price FROM accommodation WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, accommodationId);
            logger.fine("Executing find by ID statement " + statement);
            return singleItemQuery(statement);
        } catch (SQLException e) {
            logger.severe("Error finding accommodation by ID: " + e);
            throw new DBException(e);
        }
    }

    @Override
    public List<Accommodation> findAllAccommodations() {
        String query = "SELECT id, guestId, roomId, startDate, endDate, price FROM accommodation";
        try (Connection connection = dataSource.getConnection()) {
            logger.fine("Listing all accommodations");
            return multipleItemQuery(connection.prepareStatement(query));
        } catch (SQLException e) {
            logger.severe("There was an error getting all accommodations from the DB: " + e);
            throw new DBException(e);
        }
    }

    private void validateAccommodation(Accommodation accommodation) {
        if (accommodation == null) {
            throw new IllegalArgumentException("Accommodation must not be null");
        }
        if (accommodation.getGuest() == null) {
            throw new ValidationError("Accommodated guest must not be null");
        }
        if (accommodation.getRoom() == null) {
            throw new ValidationError("Room that is accommodated into must not be null");
        }
        if (accommodation.getDateFrom() == null) {
            throw new ValidationError("Accommodation start date must not be null");
        }
        if (accommodation.getDateTo() == null) {
            throw new ValidationError("Accommodation end date must not be null");
        }
        if (accommodation.getDateFrom().isAfter(accommodation.getDateTo())) {
            throw new ValidationError("Accommodation start date must not be after its end date");
        }
        if (accommodation.getTotalPrice() < 0) {
            throw new ValidationError("Accommodation total price must not be null");
        }
        logger.info("Validation check passed for accommodation " + accommodation);
    }

    private Accommodation singleItemQuery(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Accommodation accommodation = parseAccommodation(rs);
            if (rs.next()){
                throw new DBException("More guests with same id");
            }
            return accommodation;
        } else {
            return null;
        }
    }

    private List<Accommodation> multipleItemQuery(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<Accommodation> accommodations = new ArrayList<>();
        while (rs.next()) {
            accommodations.add(parseAccommodation(rs));
        }
        return accommodations;
    }

    private Accommodation parseAccommodation(ResultSet resultSet) throws SQLException {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(resultSet.getLong("id"));
        accommodation.setGuest(guestManager.findGuestById(resultSet.getLong("guestId")));
        accommodation.setRoom(roomManager.findRoomById(resultSet.getLong("roomId")));
        accommodation.setDateFrom(resultSet.getDate("startDate").toLocalDate());
        accommodation.setDateTo(resultSet.getDate("endDate").toLocalDate());
        accommodation.setTotalPrice(resultSet.getDouble("price"));
        return accommodation;
    }
}
