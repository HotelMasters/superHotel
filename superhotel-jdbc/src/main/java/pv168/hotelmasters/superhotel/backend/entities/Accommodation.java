package pv168.hotelmasters.superhotel.backend.entities;

import java.time.LocalDate;

/**
 * @author 445434
 */
public class Accommodation {
    private Long id;
    private Guest guest;
    private Room room;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Double totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this.id != null && other instanceof Accommodation && ((Accommodation) other).id.equals(this.id);
    }

    @Override
    public String toString() {
        return "[Accommodation ID " + id + "]";
    }
}
