package pv168.hotelmasters.superhotel;

import pv168.hotelmasters.superhotel.db.Utilities;
import pv168.hotelmasters.superhotel.exceptions.DBException;
import pv168.hotelmasters.superhotel.exceptions.ValidationError;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author 445434
 */
public class RoomManagerImpl implements RoomManager{
    private static final Logger logger = Logger.getLogger("RoomManagerImpl");
    private DataSource dataSource;

    public RoomManagerImpl() {
        logger.fine("Initialized RoomManagerImpl object");
    }

    public void setDataSource(DataSource source) {
        dataSource = source;
    }

    private void checkDataSource() {
        logger.info("Checking data source");
        if (dataSource == null) {
            throw new IllegalStateException("Data source must not be null");
        }
    }

    @Override
    public void createRoom(Room room) {
        logger.info("Creating room " + room);
        checkDataSource();
        logger.fine("Data source check passed");
        validateRoom(room);
        logger.fine("Room validation passed");
        createDBItem(room);
        logger.info("Room creation complete");
    }

    private void createDBItem(Room room) throws DBException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = prepareCreateStatement(connection, room);
            int updateCount = statement.executeUpdate();
            logger.fine("Room creation statement executed");
            Utilities.checkUpdateSanity(updateCount, true);
            logger.fine("Update sanity check passed");
            room.setId(Utilities.parseId(statement.getGeneratedKeys()));
            logger.info("Room " + room + " ID set to " + room.getId());
        } catch (SQLException e) {
            String errorMessage = "Error creating room in the DB: " + e;
            logger.severe(errorMessage);
            throw new DBException(errorMessage, e);
        }
    }

    private PreparedStatement prepareCreateStatement(Connection connection, Room room) throws SQLException {
        String createQuery = "INSERT INTO room (capacity, price) VALUES (?, ?)";
        logger.info("Preparing creation statement with query " + createQuery);
        PreparedStatement statement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, room.getCapacity());
        logger.fine("Set parameter at index 1 to (int) " + room.getCapacity());
        statement.setDouble(2, room.getPrice());
        logger.fine("Set parameter at index 2 to (double) " + room.getPrice());
        return statement;
    }

    @Override
    public void updateRoom(Room room) {
        logger.info("Updating room " + room);
        checkDataSource();
        logger.fine("Data source check passed");
        validateRoom(room);
        if (room.getId() == null) {
            throw new IllegalArgumentException("Room to be updated must not have null ID");
        }
        logger.fine("Room validation passed");
        updateDBItem(room);
        logger.info("Room update complete");
    }

    private void updateDBItem(Room room) throws DBException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = prepareUpdateStatement(connection, room);
            int updateCount = statement.executeUpdate();
            logger.fine("Room creation statement executed");
            Utilities.checkUpdateSanity(updateCount, false);
            logger.fine("Update sanity check passed");
        } catch (SQLException e) {
            String errorMessage = "Error updating room in the DB: " + e;
            logger.severe(errorMessage);
            throw new DBException(errorMessage, e);
        }
    }

    private PreparedStatement prepareUpdateStatement(Connection connection, Room room) throws SQLException {
        String updateQuery = "UPDATE room SET capacity = ?, price = ? WHERE id = ?";
        logger.info("Preparing update statement with query " + updateQuery);
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setInt(1, room.getCapacity());
        logger.fine("Set parameter at index 1 to (int) " + room.getCapacity());
        statement.setDouble(2, room.getPrice());
        logger.fine("Set parameter at index 2 to (double) " + room.getPrice());
        statement.setLong(3, room.getId());
        logger.fine("Set parameter at index 3 to (long) " + room.getId());
        return statement;
    }

    @Override
    public void deleteRoom(Room room) {
        logger.info("Deleting room " + room);
        checkDataSource();
        logger.fine("Data source check passed");
        if (room == null || room.getId() == null) {
            throw new IllegalArgumentException("Only deletion of existing rooms with non-null ID is implemented");
        }
        logger.fine("Room validation passed");
        deleteDBItem(room);
        logger.info("Room deletion complete");
    }

    private void deleteDBItem(Room room) throws DBException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = prepareDeleteStatement(connection, room);
            int updateCount = statement.executeUpdate();
            logger.fine("Room deletion statement executed");
            Utilities.checkUpdateSanity(updateCount, false);
            logger.fine("Update sanity check passed");
        } catch (SQLException e) {
            String errorMessage = "Error deleting room in the DB: " + e;
            logger.severe(errorMessage);
            throw new DBException(errorMessage, e);
        }
    }

    private PreparedStatement prepareDeleteStatement(Connection connection, Room room) throws SQLException {
        String updateQuery = "DELETE FROM room WHERE id = ?";
        logger.info("Preparing update statement with query " + updateQuery);
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setLong(1, room.getId());
        logger.fine("Set parameter at index 1 to (long) " + room.getId());
        return statement;
    }

    @Override
    public Room findRoomById(Long roomId) throws DBException {
        checkDataSource();
        if (roomId == null) {
            throw new IllegalArgumentException("Cannot get a room with null ID");
        }
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = prepareGetByIdStatement(connection, roomId);
            return parseSingleResult(statement.executeQuery());
        } catch (SQLException e) {
            String errorMessage = "Error getting room from the DB by ID: " + e;
            logger.severe(errorMessage);
            throw new DBException(errorMessage, e);
        }
    }

    private PreparedStatement prepareGetByIdStatement(Connection connection, Long id) throws SQLException {
        String getByIdQuery = "SELECT id, capacity, price FROM room WHERE id = ?";
        logger.info("Preparing statement with get by ID query " + getByIdQuery);
        PreparedStatement statement = connection.prepareStatement(getByIdQuery);
        statement.setLong(1, id);
        logger.fine("Set parameter at index 1 to (long) " + id);
        return statement;
    }

    @Override
    public List<Room> findRoomByCapacity(int capacity) {
        checkDataSource();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = prepareGetByCapacityStatement(connection, capacity);
            return parseMultipleResults(statement.executeQuery());
        } catch (SQLException e) {
            String errorMessage = "Error getting rooms from the DB by capacity: " + e;
            logger.severe(errorMessage);
            throw new DBException(errorMessage, e);
        }
    }

    private PreparedStatement prepareGetByCapacityStatement(Connection connection, int capacity) throws SQLException {
        String getByCapacityQuery = "SELECT id, capacity, price FROM room WHERE capacity = ?";
        logger.info("Preparing statement with get by capacity query " + getByCapacityQuery);
        PreparedStatement statement = connection.prepareStatement(getByCapacityQuery);
        statement.setLong(1, capacity);
        logger.fine("Set parameter at index 1 to (long) " + capacity);
        return statement;
    }

    @Override
    public List<Room> findAllRooms() {
        checkDataSource();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = prepareGetAllStatement(connection);
            return parseMultipleResults(statement.executeQuery());
        } catch (SQLException e) {
            String errorMessage = "Error getting all rooms from the DB: " + e;
            logger.severe(errorMessage);
            throw new DBException(errorMessage, e);
        }
    }

    private PreparedStatement prepareGetAllStatement(Connection connection) throws SQLException {
        String getAllQuery = "SELECT id, capacity, price FROM room";
        logger.info("Preparing statement with get all query " + getAllQuery);
        return connection.prepareStatement(getAllQuery);
    }

    private void validateRoom(Room room) {
        logger.info("Validating room " + room);
        if (room == null) {
            throw new IllegalArgumentException("Room object must not be null");
        }
        logger.fine("Non-null room object check passed");
        if (room.getCapacity() <= 0) {
            throw new ValidationError("Room capacity must be positive");
        }
        logger.fine("Room capacity check passed");
        if (room.getPrice() <= 0) {
            throw new ValidationError("Room price must be positive");
        }
        logger.fine("Room price check passed");
        logger.info("Room " + room + " validation passed");
    }

    private Room parseSingleResult(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Room result = parseRoom(resultSet);
            if (resultSet.next()) {
                throw new DBException("Database integrity error: Several rooms with identical ID");
            }
            return result;
        }
        return null;
    }

    private List<Room> parseMultipleResults(ResultSet resultSet) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        while (resultSet.next()) {
            rooms.add(parseRoom(resultSet));
        }
        return rooms;
    }

    private Room parseRoom(ResultSet result) throws SQLException {
        Room room = new Room();
        room.setId(result.getLong("id"));
        room.setCapacity(result.getInt("capacity"));
        room.setPrice(result.getDouble("price"));
        return room;
    }
}
