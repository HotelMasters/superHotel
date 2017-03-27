package pv168.hotelmasters.superhotel;

import java.util.logging.Logger;

/**
 * @author 445434
 */
public class Room {
    private Long id;
    private int capacity;
    private Double price;
    private final Logger logger;

    public Room() {
        logger = Logger.getLogger("Body@ " + System.identityHashCode(this));
    }

    public Long getId() {
        logger.fine("Retrieving ID");
        return id;
    }

    public void setId(Long id) {
        logger.info("Setting ID to " + id);
        this.id = id;
    }

    public int getCapacity() {
        logger.fine("Retrieving capacity");
        return capacity;
    }

    public void setCapacity(int capacity) {
        logger.info("Setting capacity to " + capacity);
        this.capacity = capacity;
    }

    public Double getPrice() {
        logger.fine("Retrieving price");
        return price;
    }

    public void setPrice(Double price) {
        logger.info("Setting price to " + price);
        this.price = price;
    }

    @Override
    public int hashCode() {
        logger.fine("Retrieving hash code");
        return id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        logger.fine("Comparing to " + other);
        return this.id != null && other instanceof Room && ((Room) other).id.equals(this.id);
    }

    @Override
    public String toString() {
        return "[Room ID " + id + "]";
    }
}
