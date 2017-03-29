package pv168.hotelmasters.superhotel.backend.entities;

/**
 * @author 445434
 */
public class Room {
    private Long id;
    private int capacity;
    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this.id != null && other instanceof Room && ((Room) other).id.equals(this.id);
    }

    @Override
    public String toString() {
        return "[Room ID " + id + "]";
    }
}
