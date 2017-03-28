package pv168.hotelmasters.superhotel.backend.impl;

import pv168.hotelmasters.superhotel.backend.entities.Accommodation;
import pv168.hotelmasters.superhotel.backend.entities.Guest;
import pv168.hotelmasters.superhotel.backend.entities.Room;
import java.time.LocalDate;

/**
 * @author Kristian Lesko
 */
public class AccommodationFactory {
    private Long id;
    private Guest guest;
    private Room room;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Double totalPrice;

    public AccommodationFactory id(Long id) {
        this.id = id;
        return this;
    }

    public AccommodationFactory guest(Guest guest) {
        this.guest = guest;
        return this;
    }

    public AccommodationFactory room(Room room) {
        this.room = room;
        return this;
    }

    public AccommodationFactory dateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public AccommodationFactory dateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public AccommodationFactory totalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public Accommodation build() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(id);
        accommodation.setGuest(guest);
        accommodation.setRoom(room);
        accommodation.setDateFrom(dateFrom);
        accommodation.setDateTo(dateTo);
        accommodation.setTotalPrice(totalPrice);
        return accommodation;
    }
}
