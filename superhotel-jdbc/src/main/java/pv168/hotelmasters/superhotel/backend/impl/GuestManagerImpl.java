package pv168.hotelmasters.superhotel.backend.impl;

import pv168.hotelmasters.superhotel.backend.interfaces.GuestManager;
import pv168.hotelmasters.superhotel.backend.entities.Guest;

import pv168.hotelmasters.superhotel.backend.exceptions.InvalidEntityException;
import pv168.hotelmasters.superhotel.backend.db.Utilities;
import pv168.hotelmasters.superhotel.backend.exceptions.DBException;
import pv168.hotelmasters.superhotel.backend.exceptions.ValidationError;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Gabriela Godiskova
 */
public class GuestManagerImpl implements GuestManager{

    private static final Logger logger = Logger.getLogger(GuestManagerImpl.class.getName());
    private DataSource dataSource;
    private final Clock clock;

    public GuestManagerImpl(Clock clock){
        this.clock = clock;
    }

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("Data source not set yet.");
        }
    }


    public void createGuest(Guest guest) throws ValidationError, InvalidEntityException {
        checkDataSource();
        validate(guest);

        if(guest.getId() != null) {
            throw new InvalidEntityException("Guest already in database");
        }
        PreparedStatement ps;
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            ps = con.prepareStatement("INSERT INTO Guest (gstname,address,birthday,cardNumber) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,guest.getName());
            ps.setString(2,guest.getAddress());
            ps.setDate(3,toSQLDate(guest.getBirthDay()));
            ps.setLong(4, guest.getCrCardNumber());
            int count = ps.executeUpdate();
            Utilities.checkUpdateSanity(count, true);

            Long id = Utilities.parseId(ps.getGeneratedKeys());
            guest.setId(id);
            con.commit();
        } catch (SQLException e) {
            String msg = "Unable to get guest into db";
            logger.log(Level.SEVERE,msg,e);
            throw new DBException(msg,e);
        }
    }

    public void updateGuest(Guest guest) throws InvalidEntityException {
        checkDataSource();
        validate(guest);
        if (guest.getId() == null) {
            throw new InvalidEntityException("Guest's id is null");
        }
        PreparedStatement ps;
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            ps = con.prepareStatement("UPDATE Guest SET gstname = ?, address = ?, birthday = ?, cardNumber = ?" +
                    "WHERE id = ?");
            ps.setString(1,guest.getName());
            ps.setString(2,guest.getAddress());
            ps.setDate(3,toSQLDate(guest.getBirthDay()));
            ps.setLong(4,guest.getCrCardNumber());
            ps.setLong(5,guest.getId());
            int count = ps.executeUpdate();
            Utilities.checkUpdateSanity(count,true);
            con.commit();
        } catch (SQLException e) {
            String msg = "Unable to update guest";
            logger.log(Level.SEVERE,msg,e);
            throw new DBException(msg,e);
        }
    }

    public void deleteGuest(Guest guest) throws InvalidEntityException {
        checkDataSource();
        if (guest == null) {
            throw new IllegalArgumentException("Guest is null");
        }
        if (guest.getId() == null) {
            throw new InvalidEntityException("Guest's id is null");
        }
        PreparedStatement ps = null;
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            ps = con.prepareStatement("DELETE FROM Guest WHERE id = ?");
            ps.setLong(1,guest.getId());
            int count = ps.executeUpdate();
            Utilities.checkUpdateSanity(count,true);
            con.commit();
        } catch (SQLException e) {
            String msg = "Unable to delete guest from db";
            logger.log(Level.SEVERE,msg,e);
            throw new DBException(msg,e);
        }
    }

    public Guest findGuestById(Long id) {
        checkDataSource();
        if (id == null) {
            throw new IllegalArgumentException("Id is null");
        }
        PreparedStatement ps;
        try (Connection con = dataSource.getConnection()) {
            ps = con.prepareStatement("SELECT id, gstname, address, birthday, cardNumber FROM Guest WHERE id = ?");
            ps.setLong(1,id);
            return singleItemQuery(ps);
        } catch (SQLException e) {
            String msg = "Unable to get body with id "+id+"from db.";
            logger.log(Level.SEVERE, msg,e);
            throw new DBException(msg,e);
        }
    }

    public List<Guest> findAllGuests() {
        checkDataSource();
        PreparedStatement ps;
        try (Connection con = dataSource.getConnection()) {
            ps = con.prepareStatement("SELECT id, gstname, address, birthday, cardNumber FROM Guest");
            return multipleItemQuery(ps);
        } catch (SQLException e) {
            String msg = "Unable to get all guests from db.";
            logger.log(Level.SEVERE,msg,e);
            throw new DBException(msg,e);
        }
    }

    private void validate(Guest guest) throws ValidationError {
        if (guest == null) {
            throw new IllegalArgumentException("Guest is null");
        }
        if (guest.getName() == null || guest.getName().length() == 0) {
            throw new ValidationError("Guest's name must be filled in.");
        }
        if (guest.getAddress() == null || guest.getAddress().length() == 0) {
            throw new ValidationError("Guest's address must be filled in.");
        }
        LocalDate today = LocalDate.now(clock);
        if (guest.getBirthDay() != null && guest.getBirthDay().isAfter(today)) {
            throw new ValidationError("Guest's birthday is in future.");
        }
    }

    private static Guest singleItemQuery(PreparedStatement ps) throws SQLException{
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Guest res = toGuest(rs);
            if (rs.next()){
                throw new DBException("More guests with same id");
            }
            return res;
        } else {
            return null;
        }
    }

    private static List<Guest> multipleItemQuery(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<Guest> guests = new ArrayList<>();
        while (rs.next()) {
            guests.add(toGuest(rs));
        }
        return guests;
    }

    private static Guest toGuest(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setId(rs.getLong("id"));
        guest.setName(rs.getString("gstname"));
        guest.setAddress(rs.getString("address"));
        guest.setBirthDay(toLocalDate(rs.getDate("birthday")));
        guest.setCrCardNumber(rs.getLong("cardNumber"));
        return guest;
    }

    private static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.toLocalDate();
        }
    }

    private static Date toSQLDate(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return Date.valueOf(date);
        }
    }


}
