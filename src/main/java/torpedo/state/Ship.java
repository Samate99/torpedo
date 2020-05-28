package torpedo.state;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Ship {
    private int x;
    private int y;
    private int width;
    private int height;

    private List<Integer> destroyed;
  /**
     * Specific of the ship
     * @param x x coordinate
     * @param y y coordinate
     * @param width an integer (the ship width)
     * @param height an integer (the ship height)
     */
    
    public Ship(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.destroyed = new ArrayList<Integer>();
    }
   /**
     *This function returns the ship opportunities
     * @param ship
     * @param space
     * @return return the
     */
    public boolean isOverlap (Ship ship, int space) {
        return x < ship.x + ship.width + space && x + width + space > ship.x && y < ship.y + ship.height + space && y + height + space > ship.y;
    }
}
